package com.example.checkscam.dto.request;

import com.example.checkscam.constant.ScamInfoType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CheckScamRequestDto {
    @NotBlank
    private String info;
    private String infoDescription; // Mô tả thông tin ( vd: tên ngân hàng, dùng để bổ sung nếu cần)
    @NotBlank
    private Integer type;
    public ScamInfoType getType() {
        return ScamInfoType.parse(type);
    }
}
