package kravchenko.danylo.plagiarism.domain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import kravchenko.danylo.plagiarism.domain.type.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize(using = PermissionSerializer.class)
public class PermissionEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", unique = true)
    private Permission permission;

}

class PermissionSerializer extends JsonSerializer<PermissionEntity> {

    @Override
    public void serialize(PermissionEntity permission,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeObject(permission.getPermission().toString());
    }
}

