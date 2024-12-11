package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.GetAllUserProfileList;
import data.UserProfile;

public class XuLy {

    private List<ThreadNhapXuat> danhSachThreadNhapXuat;
    GetAllUserProfileList getAllUserProfileList = new GetAllUserProfileList();
    private List <UserProfile> userProfileList = getAllUserProfileList.getAllUserProfileList();

    public XuLy() {
        danhSachThreadNhapXuat = new ArrayList<>();
    }

    public int getKichThuoc() {
        return danhSachThreadNhapXuat.size();
    }

    public void themVao(ThreadNhapXuat threadNhapXuat) {
        danhSachThreadNhapXuat.add(threadNhapXuat);
    }

    public void loaiRa(String userName) {
        danhSachThreadNhapXuat.removeIf(threadNhapXuat -> userName.equals(threadNhapXuat.getTenNguoiDung()));
    }

    public void guiUserNameChoClient(String userName) {
        guiMotNguoi("user#~server#~" + userName + "#~" + userName, userName);
    }

    public void guiDanhSachUserDangOnline() {
        String st = "";
        for (ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
            // Lấy fullName của người dùng thay vì userName
            String fullName = getFullName(threadNhapXuat.getTenNguoiDung());
            if (fullName != null) {
                st += fullName + "-";
            }
        }
        guiMoiNguoi("capNhatDSOnline#~server#~" + st);
    }


//    Gửi cho tất cả, bao gồm userName chính mình
    public void guiMoiNguoi(String thongDiep) {
        for (ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
            try {
                threadNhapXuat.gui(thongDiep);
            } catch (IOException e) {
                System.out.println("Lỗi ở guiMoiNguoi: " + e.getMessage());
            }
        }
    }
    /**
	 * Gửi cho tất cả, trừ userName chính mình
	 * 
	 * @param thongDiep Thông điệp cân gửi
	 * @param userID 	Định danh chính mình  (người gửi)
	 */
 // Gửi thông điệp cho một người
    public void guiMotNguoi(String thongDiep, String fullName) {
        String userName = getUserNameFromFullName(fullName); // lấy userName từ fullName
        if (userName == null) {
            System.out.println("Không tìm thấy userName cho fullName: " + fullName);
            return; // Thoát nếu không tìm thấy userName
        }
        for (ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
            if (userName.equals(threadNhapXuat.getTenNguoiDung())) {
                try {
                    threadNhapXuat.gui(thongDiep);
                    break;
                } catch (IOException e) {
                    System.out.println("Lỗi ở guiMotNguoi: " + e.getMessage());
                }
            }
        }
    }


 // Gửi thông điệp cho mọi người, trừ người gửi
    public void guiMoiNguoi(String thongDiep, String userName) {
        for (ThreadNhapXuat threadNhapXuat : danhSachThreadNhapXuat) {
            if (!userName.equals(threadNhapXuat.getTenNguoiDung())) {
                try {
                    threadNhapXuat.gui(thongDiep);
                } catch (IOException e) {
                    System.out.println("Lỗi ở gửi mọi người: " + e.getMessage());
                }
            }
        }
    }
 // Chuyển tiếp thông điệp từ một người gửi
    public void chuyenTiepThongDiep(String thongDiep, String userName) {
        String[] tachThongDiep = thongDiep.split("#~");
        // Thay thế tên người gửi bằng fullName
        String senderFullName = getFullName(userName);

        if (tachThongDiep[0].equals("guiMoiNguoi")) {
            guiMoiNguoi("guiMoiNguoi#~" + senderFullName + "#~" + tachThongDiep[2], userName);
        } else if (tachThongDiep[0].equals("guiMotNguoi")) {
            String nguoiNhan = tachThongDiep[3];
            guiMotNguoi("guiMotNguoi#~" + senderFullName + "#~" + tachThongDiep[2] + "#~" + nguoiNhan, nguoiNhan);
        }
    }


    // Lấy tên đầy đủ của người dùng từ danh sách
    public String getFullName(String userName) {
        for (UserProfile user : userProfileList) {
            if (user.getUserName().equals(userName)) {
                return user.getFullName(); // Trả về fullName của người dùng
            }
        }
        return null; // Nếu không tìm thấy người dùng
    }

    // Lấy email của người dùng từ danh sách
    public String getEmail(String userName) {
        for (UserProfile user : userProfileList) {
            if (user.getUserName().equals(userName)) {
                return user.getEmail(); // Trả về email của người dùng
            }
        }
        return null; // Nếu không tìm thấy người dùng
    }

   
    // Lấy giới tính - gender của người dùng từ danh sách
    public String getGender(String userName) {
        for (UserProfile user : userProfileList) {
            if (user.getUserName().equals(userName)) {
                return user.getGender(); // Trả về giới tính của người dùng
            }
        }
        return null; // Nếu không tìm thấy người dùng
    }
 // Lấy userName của người dùng từ fullName
    public String getUserNameFromFullName(String fullName) {
        for (UserProfile userProfile : userProfileList) {
            if (userProfile.getFullName().equals(fullName)) {
                return userProfile.getUserName(); // Trả về userName của người dùng
            }
        }
        return null; // Nếu không tìm thấy người dùng
    }
  
}

