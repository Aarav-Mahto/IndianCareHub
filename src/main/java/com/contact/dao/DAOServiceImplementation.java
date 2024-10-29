package com.contact.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

import com.contact.entity.Contact;
import com.contact.entity.QuestionAnchor;
import com.contact.entity.Register;
import com.contact.hepler.Helper;

public class DAOServiceImplementation implements DAOService {

	public static final Logger logger = Logger.getLogger(DAOServiceImplementation.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private QuestionAnchor questionAnchor;

	@Autowired
	private Helper helper;

	@Override
	@Transactional
	public int insertContact(Contact contact, QuestionAnchor questionAnchor) {
		String sql = "INSERT INTO Contact (Org_Full_Name, Org_Short_Name, pageUrl, pageTitle, Org_Icon, About_Company, Address, Web_Link,Entry_Date,Entered_By, industryName) "
				+
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int result = 0;
		try {
			result = jdbcTemplate.update(sql,
					contact.getOrg_Full_Name(),
					contact.getOrg_Short_Name(),
					contact.getPageUrl(),
					contact.getPageTitle(),
					contact.getOrg_Icon(),
					contact.getAbout_Company(),
					contact.getAddress(),
					contact.getWeb_Link(),
					contact.getEntry_Date(),
					contact.getEntered_By(),
					contact.getIndustryName());
			if (result > 0) {
				int lastInserted = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
				int n = insertNumbers(contact.getAllFields(), lastInserted);
				if (!contact.getMedia_links().isEmpty()) {
					int m = insertLinks(contact.getMedia_links(), lastInserted);
					logger.info("Total " + m + " Media Links Saved");
				}
				int qn = insertQuestionAnchor(questionAnchor, lastInserted);
				return n;
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private int insertNumbers(Map<String, String> numbers, int lastInserted) {
		String sql = "INSERT INTO ContactNumber (Company_Id, department_name, PhoneEmail) " +
				"VALUES (?, ?, ?)";
		try {
			int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) numbers.entrySet().toArray()[i];
					ps.setInt(1, lastInserted);
					ps.setString(2, entry.getKey());
					ps.setString(3, entry.getValue());
				}

				@Override
				public int getBatchSize() {
					return numbers != null ? numbers.size() : 0;
				}
			});
			return Arrays.stream(results).sum();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private int insertLinks(Map<String, String> mediaLinkMap, int lastInserted) {
		String sql = "INSERT INTO ContactSocialLinks (Company_Id, MediaName, MediaLinks) VALUES (?, ?, ?)";

		// Convert the entry set to a list for easier access by index
		List<Map.Entry<String, String>> entryList = new ArrayList<>(mediaLinkMap.entrySet());

		try {
			int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Map.Entry<String, String> entry = entryList.get(i); // Access entry by index
					ps.setInt(1, lastInserted);
					ps.setString(2, entry.getKey());
					ps.setString(3, entry.getValue());
				}

				@Override
				public int getBatchSize() {
					return mediaLinkMap != null ? mediaLinkMap.size() : 0;
				}
			});
			return Arrays.stream(results).sum(); // Sum of update counts
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private int insertQuestionAnchor(QuestionAnchor questionAnchor, int lastInserted) {
		String sql = "INSERT INTO QuestionAnchor (CompanyId, ContentType, ContentName, Content) " +
				"VALUES (?, ?, ?, ?)";
		if (helper.isNullOrEmptyMap(questionAnchor.getLink())
				&& helper.isNullOrEmptyMap(questionAnchor.getQuestion())) {
			return 0;
		}
		try {
			int result = insertQuestionAnchorData(questionAnchor.getLink(), sql, "Anchor", lastInserted);
			int result1 = insertQuestionAnchorData(questionAnchor.getQuestion(), sql, "Question", lastInserted);
			return (result + result1);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private int insertQuestionAnchorData(Map<String, String> map, String sql, String type, int lastInserted) {
		if (helper.isNullOrEmptyMap(map))
			return 0;
		try {
			int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) map.entrySet().toArray()[i];
					ps.setInt(1, lastInserted);
					ps.setString(2, type);
					ps.setString(3, entry.getKey());
					ps.setString(4, entry.getValue());
				}

				@Override
				public int getBatchSize() {
					return map.size();
				}
			});
			return Arrays.stream(results).sum();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public Map<String, Object> searchId(int searchId) {
		String sql = "SELECT * FROM Contact WHERE Entry_no = ?";

		try {
			return jdbcTemplate.query(sql, ps -> ps.setInt(1, searchId), rs -> {
				if (rs.next()) {
					Contact contact = new Contact();
					Map<String, String> fieldsMap = new LinkedHashMap<>();

					fieldsMap.put("searchId", rs.getString("Entry_no"));
					fieldsMap.put("orgName", rs.getString("Org_Full_Name"));
					fieldsMap.put("sortName", rs.getString("Org_Short_Name"));
					fieldsMap.put("pageUrl", rs.getString("pageUrl"));
					fieldsMap.put("pageTitle", rs.getString("pageTitle"));
					fieldsMap.put("org_old_logo", rs.getString("Org_Icon"));
					fieldsMap.put("orgLogo", helper.imagePath(rs.getString("Org_Icon")));
					fieldsMap.put("orgAbout", rs.getString("About_Company"));
					fieldsMap.put("orgAddress", rs.getString("Address"));
					fieldsMap.put("orgWebsite", rs.getString("Web_Link"));
					fieldsMap.put("orgEntryDate", rs.getString("Entry_Date"));
					fieldsMap.put("orgEnteredBy", rs.getString("Entered_By"));
					fieldsMap.put("lastUpdated", rs.getString("Updated"));
					fieldsMap.put("industryName", rs.getString("industryName"));

					contact.setAllFields(fieldsMap);
					contact.setDept_num(fetchNumbers(searchId));
					questionAnchor.setSocialLinkUpdate(fetchLinks(searchId));
					Map<String, Object> result = new LinkedHashMap<>();
					result.put("contact", contact);
					result.put("questionAnchor", fetchSearchedQuestionAnchor(searchId));

					return result;
				}
				return Collections.emptyMap(); // Return empty if no matching row is found
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	private Map<Integer, List<String>> fetchNumbers(int searchId) {
		String sql = "SELECT * FROM ContactNumber Where Company_Id = ?";
		try {
			return jdbcTemplate.query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, searchId);
				}
			}, new ResultSetExtractor<Map<Integer, List<String>>>() {
				@Override
				public Map<Integer, List<String>> extractData(ResultSet rs) throws SQLException {
					Map<Integer, List<String>> map = new HashMap<>();
					int i = 1;
					while (rs.next()) {
						List<String> tempList = Arrays.asList(
								rs.getString("Record_no"),
								rs.getString("Company_Id"),
								rs.getString("department_name"),
								rs.getString("PhoneEmail"));
						map.put(i, tempList);
						i++;
					}
					return map; // Return null if no matching row is found
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	private Map<Integer, List<String>> fetchLinks(int searchId) {
		String sql = "SELECT * FROM ContactSocialLinks Where Company_Id = ?";
		try {
			return jdbcTemplate.query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, searchId);
				}
			}, new ResultSetExtractor<Map<Integer, List<String>>>() {
				@Override
				public Map<Integer, List<String>> extractData(ResultSet rs) throws SQLException {
					Map<Integer, List<String>> map = new LinkedHashMap<>();
					int i = 1;
					while (rs.next()) {
						List<String> tempList = Arrays.asList(
								rs.getString("Record_no"),
								rs.getString("Company_Id"),
								rs.getString("MediaName"),
								rs.getString("MediaLinks"));
						map.put(i, tempList);
						i++;
					}
					return map; // Return null if no matching row is found
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	// Threads================
	@Override
	public Map<String, Object> pageLoading(String url) {
		String sql = "SELECT * FROM Contact Where pageUrl = ?";
		try {
			return jdbcTemplate.query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, url.trim());
				}
			}, new ResultSetExtractor<Map<String, Object>>() {
				@Override
				public Map<String, Object> extractData(ResultSet rs) throws SQLException {
					Map<String, Object> result = new HashMap<>();
					if (rs.next()) {
						Contact contact = new Contact();
						Map<String, String> map = new LinkedHashMap<>();
						int Entry_no = rs.getInt("Entry_no");
						String industryName = rs.getString("industryName");

						// map.put("searchId", rs.getString("Entry_no"));
						map.put("orgName", rs.getString("Org_Full_Name"));
						map.put("sortName", rs.getString("Org_Short_Name"));
						map.put("pageUrl", rs.getString("pageUrl"));
						map.put("pageTitle", rs.getString("PageTitle"));
						map.put("org_old_logo", rs.getString("Org_Icon"));
						map.put("orgLogo", helper.imagePath(rs.getString("Org_Icon")));
						map.put("orgAbout", rs.getString("About_Company"));
						map.put("orgAddress", rs.getString("Address"));
						// map.put("metaKeywords", rs.getString("MetaKeywords"));
						map.put("orgWebsite", rs.getString("Web_Link"));
						map.put("orgEntryDate", rs.getString("Entry_Date"));
						map.put("orgEnteredBy", rs.getString("Entered_By"));
						map.put("lastUpdated", rs.getString("Updated"));
						map.put("industryName", industryName);

						contact.setAllFields(map);
						// Threads For Three Process
						ExecutorService executor = Executors.newFixedThreadPool(5);
						Callable<Map<Integer, List<String>>> numbers = () -> fetchNumbers(Entry_no);
						Callable<Map<String, String>> links = () -> fetchMediaLinks(Entry_no);
						Callable<Map<String, String>> url = () -> fetchSeoHeading(industryName);
						Callable<QuestionAnchor> qnAnchor = () -> fetchSearchedQuestionAnchor(Entry_no);
						Callable<Map<String, String>> randomPage = () -> fetchRandomEntries(industryName);

						Future<Map<Integer, List<String>>> a1 = executor.submit(numbers);
						Future<Map<String, String>> a2 = executor.submit(links);
						Future<Map<String, String>> a3 = executor.submit(url);
						Future<QuestionAnchor> a4 = executor.submit(qnAnchor);
						Future<Map<String, String>> randomEntries = executor.submit(randomPage);

						try {
							contact.setDept_num(a1.get());
							contact.setMedia_links(a2.get());
							contact.setSeoHeading(a3.get());

							result.put("contact", contact);
							result.put("questionAnchor", a4.get());
							result.put("randomEntries", randomEntries.get());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							executor.shutdown();
						}

						return result;
					}
					return Collections.emptyMap(); // Return null if no matching row is found
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	private Map<String, String> fetchSeoHeading(String industry) {
		String sql = "SELECT * FROM SeoHeading WHERE IndustryName = ?";
		try {
			return jdbcTemplate.query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, industry); // Use setString as 'industry' is likely a String
				}
			}, new ResultSetExtractor<Map<String, String>>() {
				@Override
				public Map<String, String> extractData(ResultSet rs) throws SQLException {
					Map<String, String> map = new HashMap<>();
					if (rs.next()) {
						// Fetch SEO headings and map them
						map.put("IndustryName", rs.getString("IndustryName"));
						map.put("Heading1", rs.getString("Heading1"));
						map.put("Heading2", rs.getString("Heading2"));
						map.put("Heading3", rs.getString("Heading3"));
						map.put("Heading4", rs.getString("Heading4"));
					}
					return map;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, String> fetchMediaLinks(int searchId) {
		String sql = "SELECT * FROM ContactSocialLinks Where Company_Id = ?";
		try {
			return jdbcTemplate.query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, searchId);
				}
			}, new ResultSetExtractor<Map<String, String>>() {
				@Override
				public Map<String, String> extractData(ResultSet rs) throws SQLException {
					Map<String, String> map = new HashMap<>();
					int i = 1;
					while (rs.next()) {
						map.put(rs.getString("MediaName"), rs.getString("MediaLinks"));
					}
					return map; // Return null if no matching row is found
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}

	private QuestionAnchor fetchSearchedQuestionAnchor(int searchId) {
		String sql = "SELECT * FROM QuestionAnchor WHERE CompanyId = ?";

		try {
			return jdbcTemplate.query(sql, ps -> ps.setInt(1, searchId), rs -> {
				Map<Integer, List<String>> anchorMap = new HashMap<>();
				Map<Integer, List<String>> questionMap = new HashMap<>();
				int i = 1;

				while (rs.next()) {
					String contentType = rs.getString("ContentType");
					List<String> tempList = Arrays.asList(
							rs.getString("Sno"),
							rs.getString("CompanyId"),
							contentType,
							rs.getString("ContentName"),
							rs.getString("Content"));
					if ("Anchor".equalsIgnoreCase(contentType)) {
						anchorMap.put(i, tempList);
					} else if ("Question".equalsIgnoreCase(contentType)) {
						questionMap.put(i, tempList);
					}
					i++;
				}
				questionAnchor.setAnchorMapList(anchorMap);
				questionAnchor.setQnMapList(questionMap);
				return questionAnchor;
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Map<String, String> fetchRandomEntries(String industryName) {
		String query = "SELECT pageUrl, pageTitle FROM Contact WHERE industryName = ? ORDER BY RAND() LIMIT 5";
		Map<String, String> randomEntries = new HashMap<>();

		jdbcTemplate.query(query, ps -> {
			ps.setString(1, industryName);
		}, (rs, rowNum) -> {
			randomEntries.put(rs.getString("pageUrl"), rs.getString("pageTitle"));
			return Collections.emptyMap();
		});
		return randomEntries;
	}
	// =================================== Update Contact
	// ====================================================================

	@Override
	@Transactional
	public int updateContact(Contact contact, QuestionAnchor questionAnchor) {
		int updateId = contact.getSno();
		String sql = "UPDATE Contact SET Org_Full_Name = ?, Org_Short_Name = ?, pageUrl = ?, pageTitle = ?, Org_Icon = ?, About_Company = ?, "
				+ "Address = ?, Web_Link = ?, Updated = ?, Updated_by = ?, industryName = ? WHERE Entry_no = ?";
		int result = 0;
		if (contact.getDept_num() == null || contact.getAllFields() == null) {
			return 0;
		}
		try {
			result = jdbcTemplate.update(sql,
					contact.getOrg_Full_Name(),
					contact.getOrg_Short_Name(),
					contact.getPageUrl(),
					contact.getPageTitle(),
					contact.getOrg_Icon(),
					contact.getAbout_Company(),
					contact.getAddress(),
					contact.getWeb_Link(),
					contact.getUpdated(),
					contact.getUpdated_by(),
					contact.getIndustryName(),
					updateId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		int num = updateNumbers(contact, result, updateId);
		int qn_anchor = updateQuestionAnchor(questionAnchor, result, updateId);
		int updateSocialLinks = updatesocialLinks(questionAnchor.getSocialLinkUpdate(), result, updateId);
		return num;
	}

	private int updateNumbers(Contact contact, int success, int updateId) {
		if (success > 0) {
			String sql = "UPDATE ContactNumber SET department_name = ?, PhoneEmail = ? WHERE Record_no = ? AND Company_Id = ?";
			int[] result;
			try {
				Map<String, String> newNum = new LinkedHashMap<>();
				List<Object[]> batchArgs = new ArrayList<>();
				for (Map.Entry<Integer, List<String>> entry : contact.getDept_num().entrySet()) {
					if (!(entry.getValue().get(0).equalsIgnoreCase("empty"))) {
						batchArgs.add(new Object[] {
								entry.getValue().get(2), // department_name
								entry.getValue().get(3), // PhoneEmail
								entry.getValue().get(0), // Record_no
								entry.getValue().get(1) // Company_Id
						});
					} else {
						newNum.put(entry.getValue().get(2), entry.getValue().get(3));
					}
				}
				result = jdbcTemplate.batchUpdate(sql, batchArgs);
				int newInserts = insertNumbers(newNum, updateId);
				return Arrays.stream(result).sum() + newInserts;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		} else {
			return 0;
		}
	}

	private int updatesocialLinks(Map<Integer, List<String>> allLinks, int success, int updateId) {
		if (success > 0) {
			String sql = "UPDATE ContactSocialLinks SET MediaName = ?, MediaLinks = ? WHERE Record_no = ? AND Company_Id = ?";
			int[] result;
			try {
				Map<String, String> newLinks = new LinkedHashMap<>();
				List<Object[]> batchArgs = new ArrayList<>();
				for (Map.Entry<Integer, List<String>> entry : allLinks.entrySet()) {
					if (!(entry.getValue().get(0).equalsIgnoreCase("empty"))) {
						batchArgs.add(new Object[] {
								entry.getValue().get(2), // MediaName
								entry.getValue().get(3), // MediaLinks
								entry.getValue().get(0), // Record_no
								entry.getValue().get(1) // Company_Id
						});
					} else {
						newLinks.put(entry.getValue().get(2), entry.getValue().get(3));
					}
				}
				result = jdbcTemplate.batchUpdate(sql, batchArgs);
				int newInserts = insertLinks(newLinks, updateId);
				return Arrays.stream(result).sum() + newInserts;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		} else {
			return 0;
		}
	}

	private int updateQuestionAnchor(QuestionAnchor questionAnchor, int success, int updateId) {
		if (success > 0) {
			String sql = "UPDATE QuestionAnchor SET ContentName = ?, Content = ? WHERE Sno = ? AND CompanyId = ?";
			int[] result;
			try {
				String insertSql = "INSERT INTO QuestionAnchor (CompanyId, ContentType, ContentName, Content) " +
						"VALUES (?, ?, ?, ?)";
				Map<String, String> newQn = new LinkedHashMap<>();
				Map<String, String> newAnchor = new LinkedHashMap<>();

				List<Object[]> batchArgs = new ArrayList<>();
				for (Map.Entry<Integer, List<String>> entry : questionAnchor.getQnMapList().entrySet()) {
					if (!(entry.getValue().get(1).equalsIgnoreCase("empty"))) {
						batchArgs.add(new Object[] {
								entry.getValue().get(2), // ContentName
								entry.getValue().get(3), // Content
								entry.getValue().get(0), // Sno
								entry.getValue().get(1) // CompanyId
						});
					} else {
						newQn.put(entry.getValue().get(2), entry.getValue().get(3));
					}
				}
				result = jdbcTemplate.batchUpdate(sql, batchArgs);
				for (Map.Entry<Integer, List<String>> entry : questionAnchor.getAnchorMapList().entrySet()) {
					if (!(entry.getValue().get(1).equalsIgnoreCase("empty"))) {
						batchArgs.add(new Object[] {
								entry.getValue().get(2), // ContentName
								entry.getValue().get(3), // Content
								entry.getValue().get(0), // Sno
								entry.getValue().get(1) // CompanyId
						});
					} else {
						newAnchor.put(entry.getValue().get(2), entry.getValue().get(3));
					}
				}
				result = jdbcTemplate.batchUpdate(sql, batchArgs);
				insertQuestionAnchorData(newQn, insertSql, "Question", updateId);
				insertQuestionAnchorData(newAnchor, insertSql, "Anchor", updateId);

				return Arrays.stream(result).sum();
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		} else {
			return 0;
		}
	}

	// ======================================================================================================================
	@Override
	public boolean deleteEntry(int Entry_no) {
		String sqlContactNumber = "DELETE FROM ContactNumber WHERE Company_Id = ?";
		String sqlContact = "DELETE FROM Contact WHERE Entry_no = ?";
		try {
			// jdbcTemplate.update(sql, Entry_no);
			jdbcTemplate.update(sqlContactNumber, Entry_no);
			jdbcTemplate.update(sqlContact, Entry_no);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// =========Show Database =========================

	@Override
	public List<Map<String, String>> showDB() {
		// String sql = "SELECT * FROM Contact";
		String sql = "SELECT * FROM Contact ORDER BY Updated DESC";
		List<Map<String, String>> list = new ArrayList<>();
		try {
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					Map<String, String> tempMap = new LinkedHashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						tempMap.put(rsmd.getColumnName(i), rs.getString(i));
					}
					list.add(tempMap);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return list;
	}

	@Override
	public List<Map<String, String>> showDB_ByOrder(String order) {
		// String sql = "SELECT * FROM Contact";
		String sql = "SELECT * FROM Contact " + order;
		List<Map<String, String>> list = new ArrayList<>();
		try {
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					Map<String, String> tempMap = new LinkedHashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						tempMap.put(rsmd.getColumnName(i), rs.getString(i));
					}
					list.add(tempMap);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return list;
	}

	// ========Loading About and All Numbers==============
	@Override
	public String loadAboutOrgOfId(int id) {
		String sql = "SELECT About_Company FROM Contact WHERE Entry_no = ?";
		try {
			return jdbcTemplate.queryForObject(sql, String.class, id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (DataAccessException e) {
			throw new RuntimeException("Error querying database", e);
		}
	}

	@Override
	public Map<Integer, List<String>> loadAllContactsOfId(int id) {
		return fetchNumbers(id);
	}

	// ====================Register User/ Admin
	// ========================================
	@Override
	public boolean registrationIdExist(String rid) {
		String sql = "SELECT COUNT(*) FROM Register WHERE Unique_id = ?";
		try {
			Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), rid);
			return count != null && count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean registrationEmailIdExist(String rid) {
		String sql = "SELECT COUNT(*) FROM Register WHERE Unique_id = ?";
		try {
			Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), rid);
			return count != null && count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	@Transactional
	public int registration(Register register) {
		String sql = "INSERT INTO Register (Unique_id, FullName, Email_id, Phone, Address, User_Type, Password,Date,registered_by) "
				+
				"VALUES (?, ?, ?, ?, ?, ?, ?,?,?)";
		int result = 0;
		try {
			result = jdbcTemplate.update(sql,
					register.getUnique_id(),
					register.getFullName(),
					register.getEmail_id(),
					register.getPhone(),
					register.getAddress(),
					register.getUser_Type(),
					register.getPassword(),
					register.getDate(),
					register.getRegistered());
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return result;
	}

	@Override
	public Register findUserId(String uid) {
		String sql = "SELECT * FROM Register Where Unique_id = ?";
		boolean isValidEmail = uid.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
		if (isValidEmail) {
			return findUserEmail(uid);
		}
		try {
			return jdbcTemplate.query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, uid);
				}
			}, new ResultSetExtractor<Register>() {
				@Override
				public Register extractData(ResultSet rs) throws SQLException {
					if (rs.next()) {
						Register register = new Register();
						register.setUnique_id(rs.getString("Unique_id"));
						register.setFullName(rs.getString("FullName"));
						register.setEmail_id(rs.getString("Email_id"));
						register.setPhone(rs.getString("Phone"));
						register.setAddress(rs.getString("Address"));
						register.setUser_Type(rs.getString("User_Type"));
						register.setPassword(rs.getString("Password"));
						register.setDate(rs.getDate("Date"));
						register.setRegistered(rs.getString("registered_by"));

						return register;
					}
					return null; // Return null if no matching row is found
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Register findUserEmail(String email) {
		String sql = "SELECT * FROM Register Where Email_id = ?";
		try {
			return jdbcTemplate.query(sql, new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, email);
				}
			}, new ResultSetExtractor<Register>() {
				@Override
				public Register extractData(ResultSet rs) throws SQLException {
					if (rs.next()) {
						Register register = new Register();
						register.setUnique_id(rs.getString("Unique_id"));
						register.setFullName(rs.getString("FullName"));
						register.setEmail_id(rs.getString("Email_id"));
						register.setPhone(rs.getString("Phone"));
						register.setAddress(rs.getString("Address"));
						register.setUser_Type(rs.getString("User_Type"));
						register.setPassword(rs.getString("Password"));
						register.setDate(rs.getDate("Date"));
						register.setRegistered(rs.getString("registered_by"));

						return register;
					}
					return null; // Return null if no matching row is found
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int insertIndustrySeo(Map<String, String> h) {
		String sql = "INSERT INTO SeoHeading (IndustryName, Heading1, Heading2, Heading3, Heading4) VALUES (?, ?, ?, ?, ?)";
		int[] results;
		try {
			List<Object[]> batchArgs = new ArrayList<>();
			batchArgs.add(new Object[] { h.get("Industry"), h.get("heading1"), h.get("heading2"), h.get("heading3"),
					h.get("heading4") });
			results = jdbcTemplate.batchUpdate(sql, batchArgs);
			return Arrays.stream(results).sum();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public List<String> AllSeoedIndustry() {
		String sql = "SELECT DISTINCT IndustryName FROM SeoHeading";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@Override
	public boolean isUrl(String url) {
		String sql = "SELECT COUNT(*) FROM Contact WHERE LOWER(pageUrl) = LOWER(?)";
		try {
			Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), url);
			return count != null && count > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// ===============Implementing Search Suggession
	// =====================================================
	@Override
	public String jsonResponse(String character) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");

		String sql = "SELECT Entry_no, Org_Full_Name, Org_Short_Name, pageTitle, pageUrl " +
				"FROM Contact WHERE LOWER(Org_Full_Name) LIKE ? OR LOWER(Org_Short_Name) LIKE ? OR LOWER(pageTitle) LIKE ?";
		String searchPattern = "%" + character.toLowerCase() + "%";

		List<Map<String, Object>> resultList = jdbcTemplate.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, searchPattern);
				ps.setString(2, searchPattern);
				ps.setString(3, searchPattern);
			}
		}, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> map = new HashMap<>();
				map.put("Entry_no", rs.getInt("Entry_no"));
				map.put("PageTitle", rs.getString("pageTitle"));
				if (rs.getString("pageUrl") == null || rs.getString("pageUrl").trim().equals("")) {
					map.put("isUrlAvailable", "class='disable-url'");
				} else {
					map.put("isUrlAvailable", "");
				}
				map.put("pageUrl", rs.getString("pageUrl"));
				return map;
			}
		});

		boolean firstEntry = true;
		for (Map<String, Object> row : resultList) {
			if (!firstEntry) {
				sb.append(",");
			}
			int entryNo = (int) row.get("Entry_no");
			String url = (String) row.get("pageUrl") == null ? "" : (String) row.get("pageUrl");
			// String pageLink = "/contact/" + url.toLowerCase();
			String pageLink = (String) row.get("pageUrl") == null ? "" : "/contact/" + (String) row.get("pageUrl");

			sb.append("\"").append(entryNo).append("\":{");
			sb.append("\"pageLink\":\"").append(pageLink).append("\",");
			sb.append("\"isUrlAvailable\":\"").append(row.get("isUrlAvailable")).append("\",");
			sb.append("\"PageTitle\":\"").append(row.get("PageTitle")).append("\"}");
			firstEntry = false;
		}
		sb.append("}");
		return sb.toString();
	}

	// @Override
	// public List<Map<String, String>> sitemap() {
	// String sql = "SELECT Entry_no,Updated,Entry_Date FROM Contact";
	// List<Map<String, String>> list = new ArrayList<>();
	// try {
	// jdbcTemplate.query(sql, new RowCallbackHandler() {
	// @Override
	// public void processRow(ResultSet rs) throws SQLException {
	// Map<String, String> tempMap = new LinkedHashMap<>();
	// tempMap.put("Entry_no", rs.getString("Entry_no"));
	// String date = rs.getString("Updated") != null ? rs.getString("Updated")
	// : rs.getString("Entry_Date");
	// tempMap.put("updated", date);
	// list.add(tempMap);
	// }
	// });
	// } catch (Exception e) {
	// e.printStackTrace();
	// return Collections.emptyList();
	// }
	// return list;
	// }

	@Override
	public List<Map<String, String>> newSitemap() {
		String sql = "SELECT Entry_no, pageUrl, Updated, Entry_Date FROM Contact WHERE pageUrl IS NOT NULL";
		List<Map<String, String>> list = new ArrayList<>();
		final int batchSize = 100; // Process in batches of 100
		try {
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				int rowCount = 0;

				@Override
				public void processRow(ResultSet rs) throws SQLException {
					Map<String, String> tempMap = new LinkedHashMap<>();
					tempMap.put("Entry_no", String.valueOf(rs.getInt("Entry_no")).trim());
					tempMap.put("pageUrl", rs.getString("pageUrl").trim());
					String date = rs.getString("Updated") != null ? rs.getString("Updated")
							: rs.getString("Entry_Date");
					tempMap.put("updated", date);
					list.add(tempMap);
					rowCount++;

					// Process in batches to reduce memory footprint
					if (rowCount % batchSize == 0) {
						// Perform necessary processing here on the batch, if needed
						list.clear(); // Optionally clear list to release memory if you don’t need the entire list at
										// once
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return list;
	}

	@Override
	public List<Map<String, String>> database(String order) {
		String sql = "SELECT * FROM Contact " + order;
		List<Map<String, String>> list = new ArrayList<>();
		try {
			jdbcTemplate.query(sql, new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					Map<String, String> tempMap = new LinkedHashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						tempMap.put(rsmd.getColumnName(i), rs.getString(i));
					}
					list.add(tempMap);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return list;
	}

	@Override
	public List<Map<String, String>> databaseSearch(String charct) {
		String sql = "SELECT Entry_no, Org_Full_Name, Org_Short_Name, pageTitle, pageUrl, industryName, About_Company " +
				"FROM Contact WHERE LOWER(Org_Full_Name) LIKE ? OR LOWER(Org_Short_Name) LIKE ? OR LOWER(pageTitle) LIKE ?";
		String searchPattern = "%" + charct.toLowerCase() + "%";
		List<Map<String, String>> resultList = jdbcTemplate.query(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, searchPattern);
				ps.setString(2, searchPattern);
				ps.setString(3, searchPattern);
			}
		}, new RowMapper<Map<String, String>>() {
			@Override
			public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, String> map = new HashMap<>();
				map.put("Entry_no", ""+rs.getInt("Entry_no"));
				//String pageUrl = "https://www.indiancarehub.com/contact/" + rs.getString("pageUrl") != null ? rs.getString("pageUrl") : "";
				map.put("pageUrl", rs.getString("pageUrl"));
				map.put("pageTitle", rs.getString("pageTitle"));
				map.put("orgName", rs.getString("Org_Full_Name"));
				map.put("industryName", rs.getString("industryName"));
				map.put("aboutOrg", rs.getString("About_Company"));
				return map;
			}
		});

		return resultList;
	}

}
