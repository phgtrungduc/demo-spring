package vn.vpbanks.bo.api.config;

import lombok.*;

import java.io.File;

/**
 * Class: FuncKeyConfig
 * Description: Cau hinh function code theo file funckey.yaml
 *
 * @author quyetnv
 * @date 10/21/2025
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FuncKeyConfig {
    //Key la funciton key
    //Ten thu tuc thuc thi DB
    private String sqlProcedure;
    //queryTimeout in seconds
    private int queryTimeout = 600;
    //Dung DB standby hay active
    private boolean useStandbyDb = true;
    //Template bao cao (Neu truong hop xuat bao cao)
    private String rptTemplate;
    //Cau hinh dung streaming cua jxls hay khong (Do streamming bi gioi han 1 so formula nen khong phai luc nao cung cho stream)
    //Luu y: neu export dang pdf thi mode streaming khong the hoat dong (Do pdf chi support file xls => html => pdf)
    private boolean streaming = true;
    //Neu so luong ban ghi tu DB tra ra < minRowForStreaming thi khong nhat thiet phai dung streaming
    private int minRowForStreaming = 2000;
    //Ten duong dan va file chua css de convert tu excel ra html va pdf
    private String cssFileName = "styles".concat(File.separator).concat("pdf_style.css");
    private String headerFileName = "styles".concat(File.separator).concat("header.html");
    private String footerFileName = "styles".concat(File.separator).concat("footer.html");
    //Font family cua file html va pdf
    private String fontFamily = "Times New Roman";
    //Duong dan chua font tuong ung
    private String fontFileName = "times.ttf";
}
