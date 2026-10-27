-- noinspection SqlNoDataSourceInspectionForFile

use
scamdb;

-- Bảng users: Lưu thông tin người dùng
CREATE TABLE users
(
    id         INT PRIMARY KEY AUTO_INCREMENT,      -- Khóa chính, tự động tăng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời điểm tạo bản ghi, mặc định là thời điểm hiện tại
    name       VARCHAR(255) NOT NULL,               -- Họ và tên người dùng, không được phép NULL
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,               -- Mật khẩu người dùng, không được phép NULL
    is_active  BOOLEAN   DEFAULT TRUE               -- Trạng thái hoạt động của người dùng, mặc định là TRUE
);

-- Bảng role: Lưu thông tin vai trò của người dùng
CREATE TABLE role
(
    id   INT PRIMARY KEY AUTO_INCREMENT, -- Khóa chính, tự động tăng
    name VARCHAR(50) UNIQUE NOT NULL     -- Tên vai trò, duy nhất và không được phép NULL
);

-- Bảng user_roles: Quan hệ nhiều-nhiều giữa users và role
CREATE TABLE user_roles
(
    user_id INT,                                 -- Khóa ngoại tham chiếu đến bảng users
    role_id INT,                                 -- Khóa ngoại tham chiếu đến bảng role
    PRIMARY KEY (user_id, role_id),              -- Khóa chính ghép
    FOREIGN KEY (user_id) REFERENCES users (id), -- Ràng buộc khóa ngoại
    FOREIGN KEY (role_id) REFERENCES role (id)   -- Ràng buộc khóa ngoại
);

-- Bảng news: Lưu thông tin tin tức
CREATE TABLE news
(
    id         INT PRIMARY KEY AUTO_INCREMENT,     -- Khóa chính, tự động tăng
    url        TEXT NOT NULL,                      -- URL của tin tức, không được phép NULL
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Thời điểm tạo bản ghi, mặc định là thời điểm hiện tại
);

-- Bảng info_scam: Lưu thông tin chung về vụ lừa đảo (cả xác minh và chưa xác minh)
CREATE TABLE info_scam
(
    id          INT PRIMARY KEY AUTO_INCREMENT, -- Khóa chính, tự động tăng
    info        TEXT,                           -- Thông tin về vụ lừa đảo
    description TEXT,                           -- Mô tả chi tiết về vụ lừa đảo
    status      VARCHAR(20) DEFAULT 'pending'   -- Trạng thái của thông tin lừa đảo ('pending', 'verified', ...)
);

-- Bảng phone_scam: Lưu thông tin lừa đảo qua điện thoại (đã xác minh)
CREATE TABLE phone_scam
(
    id          INT PRIMARY KEY AUTO_INCREMENT, -- Khóa chính, tự động tăng
    info        TEXT,                           -- Thông tin lừa đảo qua điện thoại
    description TEXT                            -- Mô tả chi tiết lừa đảo qua điện thoại
);

-- Bảng bank_scam: Lưu thông tin lừa đảo qua ngân hàng (đã xác minh)
CREATE TABLE bank_scam
(
    id          INT PRIMARY KEY AUTO_INCREMENT, -- Khóa chính, tự động tăng
    info        TEXT,                           -- Thông tin lừa đảo qua ngân hàng
    description TEXT                            -- Mô tả chi tiết lừa đảo qua ngân hàng
);

-- Bảng url_scam: Lưu thông tin lừa đảo qua URL (đã xác minh)
CREATE TABLE url_scam
(
    id          INT PRIMARY KEY AUTO_INCREMENT, -- Khóa chính, tự động tăng
    info        TEXT,                           -- Thông tin lừa đảo qua URL
    description TEXT                            -- Mô tả chi tiết lừa đảo qua URL
);

-- Bảng report: Lưu thông tin báo cáo lừa đảo, liên kết với info_scam
CREATE TABLE report
(
    id           INT PRIMARY KEY AUTO_INCREMENT,         -- Khóa chính, tự động tăng
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- Thời điểm tạo bản ghi, mặc định là thời điểm hiện tại
    created_by   INT,                                    -- Khóa ngoại tham chiếu đến bảng users, người tạo báo cáo
    info         varchar(225),                                   -- Thông tin báo cáo
    description  TEXT,                                   -- Mô tả chi tiết báo cáo
    news_id      INT,                                    -- Khóa ngoại tham chiếu đến bảng news, tin tức liên quan
    info_scam_id INT,                                    -- Khóa ngoại tham chiếu đến bảng info_scam, thông tin lừa đảo liên quan
    FOREIGN KEY (created_by) REFERENCES users (id),      -- Ràng buộc khóa ngoại
    FOREIGN KEY (news_id) REFERENCES news (id),          -- Ràng buộc khóa ngoại
    FOREIGN KEY (info_scam_id) REFERENCES info_scam (id) -- Ràng buộc khóa ngoại
);

-- Bảng attachments: Lưu thông tin tệp đính kèm của báo cáo
CREATE TABLE attachments
(
    id        INT PRIMARY KEY AUTO_INCREMENT,      -- Khóa chính, tự động tăng
    report_id INT,                                 -- Khóa ngoại tham chiếu đến bảng report
    url       TEXT NOT NULL,                       -- URL của tệp đính kèm, không được phép NULL
    FOREIGN KEY (report_id) REFERENCES report (id) -- Ràng buộc khóa ngoại
);

-- Bảng report_phone_scams: Quan hệ nhiều-nhiều giữa report và phone_scam
CREATE TABLE report_phone_scams
(
    report_id     INT,                                     -- Khóa ngoại tham chiếu đến bảng report
    phone_scam_id INT,                                     -- Khóa ngoại tham chiếu đến bảng phone_scam
    PRIMARY KEY (report_id, phone_scam_id),                -- Khóa chính ghép
    FOREIGN KEY (report_id) REFERENCES report (id),        -- Ràng buộc khóa ngoại
    FOREIGN KEY (phone_scam_id) REFERENCES phone_scam (id) -- Ràng buộc khóa ngoại
);

-- Bảng report_bank_scams: Quan hệ nhiều-nhiều giữa report và bank_scam
CREATE TABLE report_bank_scams
(
    report_id    INT,                                    -- Khóa ngoại tham chiếu đến bảng report
    bank_scam_id INT,                                    -- Khóa ngoại tham chiếu đến bảng bank_scam
    PRIMARY KEY (report_id, bank_scam_id),               -- Khóa chính ghép
    FOREIGN KEY (report_id) REFERENCES report (id),      -- Ràng buộc khóa ngoại
    FOREIGN KEY (bank_scam_id) REFERENCES bank_scam (id) -- Ràng buộc khóa ngoại
);

-- Bảng report_url_scams: Quan hệ nhiều-nhiều giữa report và url_scam
CREATE TABLE report_url_scams
(
    report_id   INT,                                   -- Khóa ngoại tham chiếu đến bảng report
    url_scam_id INT,                                   -- Khóa ngoại tham chiếu đến bảng url_scam
    PRIMARY KEY (report_id, url_scam_id),              -- Khóa chính ghép
    FOREIGN KEY (report_id) REFERENCES report (id),    -- Ràng buộc khóa ngoại
    FOREIGN KEY (url_scam_id) REFERENCES url_scam (id) -- Ràng buộc khóa ngoại
);