package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opencsv.CSVReader;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class FileParseTest {

    String jsonClassPath = "src/test/resources/card.json";
    String zipClassPath = "src/test/resources/Files.zip";

    @Test
    void pdfTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("Witcher.pdf");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        PDF pdf = new PDF(inputStream);
        assertThat(pdf.text).contains("Анджей Сапковский");
    }

    @Test
    void xlsxTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("Order.xlsx");
        InputStream inputStream = zipFiles.getInputStream(zipEntry);
        XLS xls = new XLS(inputStream);
        assertThat(xls.excel
                .getSheetAt(0)
                .getRow(4)
                .getCell(1)
                .getStringCellValue()).contains("Суп лапша");
    }

    @Test
    void parseCsvTest() throws Exception {
        ZipFile zipFiles = new ZipFile(zipClassPath);
        ZipEntry zipEntry = zipFiles.getEntry("Order1.csv");
        try (InputStream inputStream = zipFiles.getInputStream(zipEntry);
             CSVReader csv = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> content = csv.readAll();
            assertThat(content.get(2)).contains("1;Креветки в кляре");
        }


        //@Test
        //void jsonTest() {
        //InputStream is = classLoader.getResourseAsStream("card.json");
        //Gson gson = new Gson();
       // JsonObject jsonObject = gson.fromJson(new InputStreamReader(is), JsonObject.class);
        //assertThat(jsonObject.get("name").getAsString()).isEqualTo("Radmir");
        //assertThat(jsonObject.get("isGoodStudent").getAsBoolean()).isEqualTo(true);
       // }

        @Test
        void parseJsonTest() throws Exception{
            ObjectMapper mapper = new ObjectMapper();
            Staff staff = mapper.readValue(Paths.get(jsonClassPath).toFile(), Staff.class);
            assertThat(staff.name).isEqualTo("Radmir");
    }
}

