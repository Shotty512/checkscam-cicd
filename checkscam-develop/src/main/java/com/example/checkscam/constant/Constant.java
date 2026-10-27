package com.example.checkscam.constant;

public class Constant {

    public static final String PROMPT_CHATBOT_IN_FIRST = "**YÊU CẦU TUYỆT ĐỐI:** Chỉ trả về MỘT đối tượng JSON hợp lệ duy nhất. " +
            "KHÔNG bao gồm bất kỳ văn bản, giải thích, hoặc ký tự markdown (như ```json) nào bên ngoài đối tượng JSON.\n\n**ĐỊNH DẠNG JSON BẮT BUỘC:" +
            "**\n```json\n{\n  \"type\": <number>,\n  \"typeScam\": <number_or_null>,\n  \"content\": \"<string>\"\n}\n```\n\n**QUY TẮC ĐIỀN GIÁ TRỊ:**\n\n1.  **`type`** (number):\n    " +
            "* `1`: Nếu đầu vào là yêu cầu kiểm tra lừa đảo (số điện thoại, số tài khoản, URL).\n    " +
            "* `2`: Nếu đầu vào là một câu hỏi thông thường khác.\n\n2.  **`typeScam`** (number or `null`):\n    " +
            "* Nếu `type` là `1`:\n        * `1`: Nếu thông tin kiểm tra là số điện thoại.\n       " +
            " * `2`: Nếu thông tin kiểm tra là số tài khoản ngân hàng.\n        * `3`: Nếu thông tin kiểm tra là URL.\n   " +
            " * Nếu `type` là `2`: Giá trị là `null` (không phải chuỗi \"null\").\n\n3.  **`content`** (string):\n    " +
            "* Nếu `type` là `1`: Chính xác chuỗi thông tin cần kiểm tra (ví dụ: \"0123456789\", \"[https://abc.xyz](https://abc.xyz)\"). KHÔNG thêm mô tả.\n    " +
            "* Nếu `type` là `2`: Câu trả lời trực tiếp cho câu hỏi. KHÔNG lặp lại câu hỏi.\n\n**VÍ DỤ CỤ THỂ:**\n\n1.  Đầu vào: \"0123456789 có phải lừa đảo không?\"\n   " +
            " Kết quả JSON:\n    ```json\n    {\\\"type\\\": 1, \\\"typeScam\\\": 1, \\\"content\\\": \\\"0123456789\\\"}\n    ```\n\n" +
            "2.  Đầu vào: \"[https://abcxyz.vip](https://abcxyz.vip) " +
            "có an toàn không?\"\n    Kết quả JSON:\n    ```json\n    {\\\"type\\\": 1, \\\"typeScam\\\": 3, \\\"content\\\": \\\"https://abcxyz.vip\\\"}\n    ```\n\n3.  Đầu vào: \"HTTP hoạt động thế nào?\"\n    " +
            "Kết quả JSON:\n    ```json\n    {\\\"type\\\": 2, \\\"typeScam\\\": null, \\\"content\\\": \\\"HTTP là giao thức truyền thông client-server, trong đó client gửi yêu cầu và server gửi phản hồi.\\\"}\n    ```\n\n" +
            "**LƯU Ý QUAN TRỌNG:** Toàn bộ phản hồi của bạn PHẢI LÀ đối tượng JSON này ({\n" +
            "  \"type\": ,\n" +
            "  \"content\": ,\n" +
            "  \"typeScam\": \n" +
            "}.) và  dữ liệu bạn cần xử lý là:" ;
    public static final String PROMPT_CHATBOT_IN_CHECKSCAM = "Tôi sẽ đưa bạn một đoạn dữ liệu thông tin về lừa đảo ( sđt, stk , url) bạn hãy " +
            "chỉnh sửa nó thành một đoạn văn bản hoàn chỉnh, dễ hiểu, thân thiện để cho người dùng dễ đọc với fomat như sau: " +
            "VD:UrlScamStatsInfoDto(id=41597, urlScam=apple.appletwpoorf.com, verifiedCount=1, lastReportAt=null) bạn sẽ chuyển thành như sau:" +
            "đường dẫn lừa đảo apple.appletwpoorf.com đã được xác thực lừa đảo từ 1 người dùng, chưa có thông tin chi tiết về đường dẫn này" +
            "VD2: PhoneScamStatsInfoDto(id=20, phoneNumber=0123456787, lastReportAt=2025-05-07T08:52:08, verifiedCount=1, reasonsJson=ReasonsJsonDto(reasons=[ReasonsJsonDto.Reason(name=Giả mạo công an, quantity=1)]))" +
            "bạn sẽ chuyển thành như sau: số điện thoại 0123456787 đã được xác thực lừa đảo từ 1 người dùng, với lý do giả mạo công an" +
            "Lưu ý chỉ đưa ra câu trả lời theo mẫu và không thêm gì ( có thể sửa lại câu chữ cho dễ hiểu hơn)," ;
}
