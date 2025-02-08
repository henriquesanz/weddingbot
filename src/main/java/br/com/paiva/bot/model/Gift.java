package br.com.paiva.bot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gifts")
@Data
public class Gift {

    @Id
    private Long id;
    private String name;
    private String status;
    private String reservedBy;

    public Gift(Long id, String giftName, String status, String reservedBy) {
        this.id = id;
        this.name = giftName;
        this.status = status;
        this.reservedBy = reservedBy;
    }

    public Gift(String giftName, String status, String reservedBy) {
        this.name = giftName;
        this.status = status;
        this.reservedBy = reservedBy;
    }

}
