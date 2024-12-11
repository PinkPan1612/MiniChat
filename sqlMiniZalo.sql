create DATABASE ChatApp;
USE ChatApp;

-- Bảng users
create TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(50) NOT NULL,
    passW VARCHAR(50) NOT NULL,
    fullName VARCHAR(100),
    email VARCHAR(100),
    avatar VARCHAR(255),
    gender varchar(20)
);
-- Bảng messages
CREATE TABLE messages (
    idMessage INT AUTO_INCREMENT PRIMARY KEY,
    idSender INT,
    dateSend DATETIME,
    dateRead DATETIME,
    content TEXT,
    typeMess ENUM('text', 'image', 'file', 'emoji'),  -- ví dụ kiểu tin nhắn
    FOREIGN KEY (idSender) REFERENCES users(id) ON DELETE CASCADE
);
-- Bảng messageRecipients
CREATE TABLE messageRecipients (
    idMessage INT,
    idRecipient INT,
    PRIMARY KEY (idMessage, idRecipient),
    FOREIGN KEY (idMessage) REFERENCES messages(idMessage) ON DELETE CASCADE,
    FOREIGN KEY (idRecipient) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO users (userName, passW, fullName, email, avatar, gender) VALUES
('huynguyen881', 'huynguyen881', 'Nguyễn Huy', 'huynguyen881@gmail.com', null, 'nam'),
('hominhtri', 'hominhtri', 'Hồ Minh Trí', 'hominhtri@gmail.com', null, 'nam'),
('nguyenkhanhhuy', 'nguyenkhanhhuy', 'Nguyễn Khánh Huy', 'nguyenkhanhhuy@gmail.com', null, 'nam'),
('tran_thi_d', 'matkhau321', 'Trần Thị D', 'tranthid@example.com', null, 'nữ');
INSERT INTO messages (idSender, dateSend, dateRead, content, typeMess) VALUES
(1, '2024-11-07 10:00:00', '2024-11-07 10:05:00', 'Xin chào, bạn khoẻ không?', 'text'),
(2, '2024-11-07 10:02:00', '2024-11-07 10:06:00', 'Mình khoẻ, cảm ơn!', 'text'),
(3, '2024-11-07 10:03:00', NULL, 'Đây là một hình ảnh', 'image'),
(4, '2024-11-07 10:04:00', NULL, 'Xem tài liệu này', 'file');
INSERT INTO messageRecipients (idMessage, idRecipient) VALUES
(1, 2),
(2, 1),
(3, 2),
(3, 4),
(4, 3);
