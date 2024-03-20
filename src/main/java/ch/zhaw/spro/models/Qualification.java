package ch.zhaw.spro.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Qualification class models the concept of a professional or educational qualification within the system.
 * Each instance of this class represents a unique qualification, identifiable by its name and a MongoDB-generated ID.
 * <p>
 * This class is mapped to the "qualifications" collection in MongoDB, meaning each Qualification object corresponds to a document in this collection.
 *
 */

@Document(collection = "qualifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Qualification {

    @Id
    private String id;
    private String name;

    /**
     * Constructs a new Qualification object with the specified qualification name.
     * This constructor is intended for cases where only the name of the qualification is known at the time of object creation.
     *
     * @param qualification The name of the qualification.
     */
    public Qualification(String qualification) {
        this.name = qualification;
    }
}