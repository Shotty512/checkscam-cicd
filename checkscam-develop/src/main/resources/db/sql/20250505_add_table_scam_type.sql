CREATE TABLE scam_types (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            code VARCHAR(20) NOT NULL UNIQUE COMMENT 'Mã định danh của loại lừa đảo',
                            name VARCHAR(255) NOT NULL COMMENT 'Tên loại lừa đảo'
);
