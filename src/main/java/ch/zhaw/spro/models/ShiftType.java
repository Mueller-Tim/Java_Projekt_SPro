package ch.zhaw.spro.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a type of shift in the scheduling system.
 * This class encapsulates the properties of a shift type, including its name, color, and required qualifications.
 */

@Document(collection = "shiftType")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ShiftType {

    @Id
    private String id;
    private String name;
    private String color;
    private String[] qualifications;

    /**
     * Returns a list of qualification IDs required for this shift type.
     * This method converts the array of qualifications into a List for easier manipulation.
     *
     * @return A List of String representing qualification IDs.
     */
    public List<String> getQualificationIds(){
        return Arrays.stream(qualifications).toList();
    }
}
