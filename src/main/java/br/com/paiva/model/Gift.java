package br.com.paiva.model;

import com.azure.core.annotation.Get;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gifts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gift {

    @Id
    private Long id;
    private String name;
    private String status;
    private String reservedBy;

    public Gift(String giftName, String status, String reservedBy) {
        this.name = giftName;
        this.status = status;
        this.reservedBy = reservedBy;
    }

}
