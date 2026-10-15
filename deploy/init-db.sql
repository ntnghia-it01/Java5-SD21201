-- Chạy lại an toàn: Hibernate tạo bảng sau khi database đã tồn tại.
IF DB_ID(N'$(DB_NAME)') IS NULL
BEGIN
    CREATE DATABASE [$(DB_NAME)];
END;
GO
SELECT name FROM sys.databases WHERE name = N'$(DB_NAME)';
GO
