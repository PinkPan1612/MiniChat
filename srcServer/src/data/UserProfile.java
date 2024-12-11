package data;

public class UserProfile {
	 private String userName;
	    private String fullName;
	    private String email;
	    private String gender;
	    private String passw;
	    private int userID;
	    private String avatar;

	
	public UserProfile(String userName, String fullName, String email, String gender, String passW
			,int userID, String avatar) {
		this.userID = userID;
        this.userName = userName;
        this.passw = passW;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.avatar = avatar;
    }


	// Getter methods
    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }


	public String getPassw() {
		return passw;
	}


	public int getUserID() {
		return userID;
	}


	public String getAvatar() {
		return avatar;
	}
    
}
