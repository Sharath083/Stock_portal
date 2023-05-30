package Methods;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import Pojo.FeedbackPojo;
import Pojo.RecommendationPojo;
import Pojo.UserPojo;

public class UserMethods {
	
	
    private static final String EMAIL_PASSWORD_CHECK="select email,pnumber from usertable where email=? or pnumber=?";
    private static final String USERNAME_CHECK="select username from usertable where username=?";
    private static final String CREATING_USER="insert into usertable (username,password,email,pnumber) values(?,?,?,?)";
        
    private static final String LOGIN_CHECK="select username,password from usertable where username=? and password=?";

    private static final String INSERT_TOKEN="insert into tokentable (userid,sessionToken) values (?,?)";
    
    private static final String ADDING_RECOMMENDATION="INSERT INTO recommendation (userid, stocksymbol, rtype,rdate, rdetails) VALUES (?,?,?,?,?)";

    private static final String VIEW_RECCOMENDATION="select rid,stocksymbol, rtype, rdate, rdetails from recommendation where userid=?";
    
    private static final String FILTER="SELECT * FROM recommendation where rid=? or rtype=? or stocksymbol=? or rdate=?";
    
    private static final String TOKEN_QUERY="select userid from tokentable where sessionToken=?";

    private static final String GET_TOKEN="select sessionToken from tokentable where userid=?";
    private static final String GET_USERID="select userid from usertable where username=?";
    
    private static final String DELETE_TOKEN="DELETE from tokentable where userid=?";
    
    private static final String FEEDBACK="insert into feedback (userid,rid,rating) values (?,?,?)";
    
    
    private static final String DELETE_USER_TOKENTABLE="DELETE from tokentable where userid=? ";
    private static final String DELETE_USER_RECOMMENDATION="DELETE from recommendation where userid=?";
    private static final String DELETE_USER_USERTABLE="DELETE from usertable where userid=?";

    
    private static final  String STOCK_CHECK="select rid from recommendation where rid=?";

    
    
    
    
	static Logger log = Logger.getLogger("UserMethods");        
    public static void logMethod() {
		try {
			FileHandler fh = new FileHandler("D:\\Research Program logs\\log.txt");
	        log.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);

		} catch (Exception e) {
			log.info("Exception in logMethod");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }
	
	 public boolean emailPhoneVal(String email,String pNumber,Connection con) {
		 	PreparedStatement ps = null;
		 	ResultSet rs=null;
	        try {
	            ps=con.prepareStatement(EMAIL_PASSWORD_CHECK);
	            ps.setString(1, email);
	            ps.setString(2, pNumber);
	            rs=ps.executeQuery();
	            while(!(rs.next())) {
	                 String regex = "^[A-Za-z0-9+_.-]+@(.+)$"; 
	                 Pattern pattern = Pattern.compile(regex);  
	                 Matcher matcher = pattern.matcher(email);
	                if(matcher.matches() && numberVal(pNumber)&& pNumber.length()==10) {
	                	log.info("email amd phone are validated");
	                    return true;                    
	                }
	                else {
	                    return false;
	                }
	            }
	        } catch (SQLException e) {
	        	log.info("Exception in emailphone validation");
	            e.printStackTrace();
	        }
	        finally {
				try {
					ps.close();
					rs.close();
				} catch (SQLException e) {
		        	log.info("Exception while closing prepareStatement,Resultset");
					e.printStackTrace();
				}
			}
	        return false;
	        
	    }
	 
	 
	    public boolean numberVal(String pNumber) {
	        
	        
	        try {
	            @SuppressWarnings("unused")
				double d = Double.parseDouble(pNumber);
	        } catch (NumberFormatException nfe) {
	            return false;
	        }
	        return true;
	        
	    }
	
	
	
	public boolean validUserName(String userName,Connection con) {
		PreparedStatement ps = null;
	 	ResultSet rs=null;
		try {
			ps=con.prepareStatement(USERNAME_CHECK);
			ps.setString(1, userName);
			rs=ps.executeQuery();
			while(!(rs.next())) {
				if(userName.length()>=3 && userName.length()<=10 && StringUtils.isAlphanumeric(userName)) {
					log.info("UserNAme has been Validated");
					return true;
				}
				else {
					return false;
				}
			}
			
		} catch (SQLException e) {
			log.info("Exception in validUser");		
			e.printStackTrace();
		}
		finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
		return false;
	}
	public boolean validPassword(String pass,Connection con) {
		if(pass.length()>=6 &&StringUtils.isAlphanumeric(pass)) {
			log.info("Password has been validated");
			return true;
		}
		else {
			return false;
		}

	}

	
	public void insertDetails(UserPojo up,Connection con) {
		logMethod();
		PreparedStatement ps = null;
		try {
			ps=con.prepareStatement(CREATING_USER);
			ps.setString(1,up.getUsername());
			ps.setString(2,up.getPassword());
			ps.setString(3,up.getEmail());
			ps.setString(4,up.getPhonenumber());
			ps.execute();
			log.info("A new user has been added");
		} catch (SQLException e) {
        	log.info("Exception while inserting data in user table");
			e.printStackTrace();
		}
		finally {
			try {
				ps.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}

	}
	
	public void insertToken(int userid,Connection con) {
		PreparedStatement ps2 = null;
		logMethod();
		String sessionToken = UUID.randomUUID().toString();

		try {
			ps2 = con.prepareStatement(INSERT_TOKEN);
			ps2.setInt(1,userid);
			ps2.setString(2, sessionToken);
			ps2.execute();
			log.info("token and userid has added to token table");
		} catch (SQLException e) {
			log.info("Exception in insertToken");
			e.printStackTrace();
		}
		finally {
			try {
				ps2.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
		
	}
	public String displayToken(int userid,Connection con) {
		logMethod();
		PreparedStatement ps = null;
	 	ResultSet rs=null;
    	 try {
	            ps=con.prepareStatement(GET_TOKEN);
	            ps.setInt(1, userid);
	            rs=ps.executeQuery();
	            String token = null;
	            while(rs.next()) {
	            	token=rs.getString("sessionToken");
	            }
	            return token;
    	 }catch (Exception e) {
    		 log.info("Exception in displayToken");
		}
    	 finally {
				try {
					ps.close();
					rs.close();
				} catch (SQLException e) {
		        	log.info("Exception while closing prepareStatement,Resultset");
					e.printStackTrace();
				}
			}
		return null;


	}
	
	 public String userloginval(String username,String password,Connection con) {
		logMethod();
		PreparedStatement ps2 = null;
		ResultSet rs1=null;
		PreparedStatement ps = null;
	 	ResultSet rs=null;
        try {
            ps=con.prepareStatement(LOGIN_CHECK);
            ps.setString(1, username);
            ps.setString(2, password);
            rs=ps.executeQuery();
            while((rs.next())) {
            	PreparedStatement ps1=con.prepareStatement(GET_USERID);
	            ps1.setString(1, username);
	            rs1=ps1.executeQuery();
        		String token=null;
	            	while(rs1.next()) {
	            		delete(rs1.getInt("userid"), con);
		           		try {
		           			token=sessiontoken();
		           			ps2 = con.prepareStatement(INSERT_TOKEN);
		           			ps2.setInt(1,rs1.getInt("userid"));
		           			ps2.setString(2,token);
		           			ps2.execute();
		           			log.info("User "+username +" has logged in and token has generated");
		           		}catch (Exception e) {
		           			log.info("Exception while inserting userid and token in token table");
						}
		           		
	            }		
            	return token;   
            }
        } catch (SQLException e) {
        	log.info("Exception while loginValidation");
            e.printStackTrace();
        }
        finally {
			try {
				ps2.close();
				ps.close();
				rs.close();
				rs1.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
        
        return null;
        
    }
	 public String sessiontoken()
	 {
		   String sessionToken = UUID.randomUUID().toString();

		return (String) sessionToken.subSequence(1, 15);
	 }
	 
	public boolean insertDetails(RecommendationPojo rp,int userId,Connection con) {
		logMethod();
			PreparedStatement ps1 = null;
			try {
				ps1 = con.prepareStatement(ADDING_RECOMMENDATION);
				ps1.setInt(1,userId);
				ps1.setString(2, rp.getStocksymbol());
				ps1.setString(3, rp.getRtype());
				ps1.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
				ps1.setString(5, rp.getRdetails());
				ps1.execute();
				log.info("A User has submitted reccomendation");
				return true;
			} catch (SQLException e) {
				log.info("Exception while inserting data into recommondation table");
				e.printStackTrace();
				return false;
			}	
			finally {
				try {
					ps1.close();
				} catch (SQLException e) {
		        	log.info("Exception while closing prepareStatement,Resultset");
					e.printStackTrace();
				}
			}
			
	}
	 
//		public int viewDetails(RecommendationPojo rp,Connection con) {
//			String tokenVal="select userid from tokentable where sessionToken=?";
//			try {
//				PreparedStatement ps=con.prepareStatement(tokenVal);
//				ps.setString(1, rp.getSessionToken());
//				ResultSet  rs=ps.executeQuery();
//				while(rs.next()) {
//					
//					return rs.getInt("userid");
//					
//				}				
//			}catch (Exception e) {
//				// TODO: handle exception
//			}
//			return -1;
//			
//		}
	public List<RecommendationPojo> list(int userid,Connection con){
		logMethod();
		ArrayList<RecommendationPojo> list=new ArrayList<>();
		PreparedStatement ps1 = null;
		ResultSet rs1=null;
		try {
			ps1 = con.prepareStatement(VIEW_RECCOMENDATION);
			ps1.setInt(1,userid);
			rs1=ps1.executeQuery();
			while(rs1.next()) {
				list.add(new RecommendationPojo(rs1.getInt("rid"),rs1.getString("stocksymbol"),rs1.getString("rdate"), rs1.getString("rtype"),rs1.getString("rdetails")));
			}
			log.info("A User has viewed Reccomendation");
			return list;
		} catch (SQLException e) {
			log.info("Exception in list method ");
			e.printStackTrace();
		}
		finally {
			try {
				ps1.close();
				rs1.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
		return null;		
	}
	

	public List<RecommendationPojo> filter(RecommendationPojo rp,Connection con) {
		logMethod();
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		ArrayList<RecommendationPojo> list=new ArrayList<>();
		try {
			ps1=con.prepareStatement(FILTER);
			ps1.setInt(1,rp.getRid());
			ps1.setString(2, rp.getRtype());
			ps1.setString(3, rp.getStocksymbol());
			ps1.setString(4, rp.getRdate());
			rs1=ps1.executeQuery();
			while(rs1.next()) {
				list.add(new RecommendationPojo(rs1.getInt("rid"),rs1.getString("stocksymbol"),rs1.getString("rdate"), rs1.getString("rtype"),rs1.getString("rdetails")));
			}
			log.info("A user has applied filter");
			return list;
			
		} catch (SQLException e) {
			log.info("Exception in filter method ");
			e.printStackTrace();		
		}
		finally {
			try {
				ps1.close();
				rs1.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public String logoutFromAll(String username,String password,Connection con) {
		logMethod();
		PreparedStatement ps;
		ResultSet rs;
        try {
            ps=con.prepareStatement(LOGIN_CHECK);
            ps.setString(1, username);
            ps.setString(2, password);
            rs=ps.executeQuery();
            ResultSet rs1=null;
            
            PreparedStatement ps1;
            while((rs.next())) {
            	ps1=con.prepareStatement(GET_USERID);
	            ps1.setString(1, username);
	            rs1=ps1.executeQuery();{
	            PreparedStatement ps2 = null;
	            while(rs1.next()) {
	            	delete(rs1.getInt("userid"), con);	            		
	            	String token=sessiontoken();
	           		try {
	           			ps2 = con.prepareStatement(INSERT_TOKEN);
	           			ps2.setInt(1,rs1.getInt("userid"));
	           			ps2.setString(2,token);
	           			ps2.execute();
	           			log.info("User "+username+" has logged out from all devices");
	           		} catch (SQLException e) {
    		        	log.info("Exception while inserting in token table of logoutAll method ");
	           			e.printStackTrace();
	           		}
	           		finally {
	    				try {
	    					ps.close();
	    					rs.close();
	    					ps1.close();
	    					ps2.close();
	    				} catch (SQLException e) {
	    		        	log.info("Exception while closing prepareStatement,Resultset");
	    					e.printStackTrace();
	    				}
	    			}
	            		
	            	            		
	                	return token;   

	            	}
	            }
            }
        } catch (SQLException e) {
        	log.info("Exception in logoutAll method ");
			e.printStackTrace();            
        }        
        return null;
        
    }
	public void delete(int userid,Connection con) {
		logMethod();
		PreparedStatement ps=null;
		try {
			ps=con.prepareStatement(DELETE_TOKEN);
			ps.setInt(1, userid);
			ps.execute();
		} catch (SQLException e) {
			log.info("Exception while deleting token");
			e.printStackTrace();
		}
		finally {
			try {
				ps.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
	}
	
	
	public void feedback(FeedbackPojo fp,int userId,Connection con) {
		logMethod();
		PreparedStatement ps1 = null;
		try {
			
				ps1=con.prepareStatement(FEEDBACK);
				ps1.setInt(1, userId);
				ps1.setInt(2, fp.getRid());
				ps1.setString(3, fp.getRating());
				ps1.execute();	
				log.info("A user has given feedback on product "+fp.getRid());
			
		} catch (SQLException e) {
        	log.info("Exception in Feedback Method  ");

			e.printStackTrace();
			
		}
		finally {
			try {
				ps1.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
		
	}
	
	public int tokenValidation(String token,Connection con) {
		logMethod();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps=con.prepareStatement(TOKEN_QUERY);
			ps.setString(1, token);
			rs=ps.executeQuery();
			while(rs.next()) {
				
				return rs.getInt("userid");
			}
		} catch (SQLException e) {
			log.info("Exception in token Validation");	
			e.printStackTrace();
		}
		finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
	        	log.info("Exception while closing prepareStatement,Resultset");
				e.printStackTrace();
			}
		}
		return -1;
		
	}
	
	public boolean deleteAccount(String username,String password,Connection con) {
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement psUsertable = null;
		PreparedStatement psToken = null;
		PreparedStatement psRec = null;
	 	ResultSet rs=null;
	 	ResultSet rs1=null;

        try {
            ps=con.prepareStatement(LOGIN_CHECK);
            ps.setString(1, username);
            ps.setString(2, password);
            rs=ps.executeQuery();
            while((rs.next())) {
            	try {
            		ps1=con.prepareStatement(GET_USERID);
    	            ps1.setString(1, username);
    	            rs1=ps1.executeQuery();
            		
            	}catch (Exception e) {
            		log.info("Exception in getting userid in delete account");
            		e.printStackTrace();
            		return false;

            	}
            	
            }
            while(rs1.next()) {
            	
                psUsertable=con.prepareStatement(DELETE_USER_USERTABLE);
                psToken=con.prepareStatement(DELETE_USER_TOKENTABLE);
                psRec=con.prepareStatement(DELETE_USER_RECOMMENDATION);
                psUsertable.setInt(1,rs1.getInt("userid"));
                psToken.setInt(1,rs1.getInt("userid"));
                psRec.setInt(1,rs1.getInt("userid"));
                psToken.execute();
                psRec.execute();
                psUsertable.execute();  
                log.info("user with username "+username+" has been deleted");
                return true;
            }
            
        }catch (Exception e) {
        	log.info("Exception in delete account");
			e.printStackTrace();
    		return false;		
        }
        finally {
			try {
				ps.close();
				ps1.close();
				psRec.close();
				psToken.close();
				psUsertable.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return false;
	}
	public boolean checkStock(int id,Connection con) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps=con.prepareStatement(STOCK_CHECK);
			ps.setInt(1, id);
			rs=ps.executeQuery();
			while(rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;

		}finally {
			try {
				ps.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
		
	}
	
	
}









































