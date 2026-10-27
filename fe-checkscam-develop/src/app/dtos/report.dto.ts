export interface ReportDTO {
    info: string;
    emailAuthorReport: string;
    type: number;
    reason: string;
    infoDescription: string;
    captchaToken: string;
    info2?: string; // Tên chủ tài khoản nếu có
    info3?: string; // Ngân hàng nếu có

    // >> Bổ sung: Thêm thuộc tính pageToReport vào đây
    pageToReport: string; // Hoặc pageToReport?: string; nếu trường này không bắt buộc gửi lên backend
}