package com.amplifyframework.datastore.generated.model;


import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Infection type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Infections")
@Index(name = "undefined", fields = {"id","infectedTimestamp"})
@Index(name = "GetInfectionsByUser", fields = {"userId"})
public final class Infection implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField INFECTED_TIMESTAMP = field("infectedTimestamp");
  public static final QueryField USER_ID = field("userId");
  public static final QueryField FROM_INFECTION_ID = field("fromInfectionId");
  public static final QueryField DETECTION_SOURCE = field("detectionSource");
  public static final QueryField CREATED_TIMESTAMP = field("createdTimestamp");
  public static final QueryField DELETED_TIMESTAMP = field("deletedTimestamp");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="AWSTimestamp", isRequired = true) Long infectedTimestamp;
  private final @ModelField(targetType="ID") String userId;
  private final @ModelField(targetType="ID") String fromInfectionId;
  private final @ModelField(targetType="DetectionSource", isRequired = true) DetectionSource detectionSource;
  private final @ModelField(targetType="AWSTimestamp") Long createdTimestamp;
  private final @ModelField(targetType="AWSTimestamp") Long deletedTimestamp;
  public String getId() {
      return id;
  }
  
  public Long getInfectedTimestamp() {
      return infectedTimestamp;
  }
  
  public String getUserId() {
      return userId;
  }
  
  public String getFromInfectionId() {
      return fromInfectionId;
  }
  
  public DetectionSource getDetectionSource() {
      return detectionSource;
  }
  
  public Long getCreatedTimestamp() {
      return createdTimestamp;
  }
  
  public Long getDeletedTimestamp() {
      return deletedTimestamp;
  }
  
  private Infection(String id, Long infectedTimestamp, String userId, String fromInfectionId, DetectionSource detectionSource, Long createdTimestamp, Long deletedTimestamp) {
    this.id = id;
    this.infectedTimestamp = infectedTimestamp;
    this.userId = userId;
    this.fromInfectionId = fromInfectionId;
    this.detectionSource = detectionSource;
    this.createdTimestamp = createdTimestamp;
    this.deletedTimestamp = deletedTimestamp;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Infection infection = (Infection) obj;
      return ObjectsCompat.equals(getId(), infection.getId()) &&
              ObjectsCompat.equals(getInfectedTimestamp(), infection.getInfectedTimestamp()) &&
              ObjectsCompat.equals(getUserId(), infection.getUserId()) &&
              ObjectsCompat.equals(getFromInfectionId(), infection.getFromInfectionId()) &&
              ObjectsCompat.equals(getDetectionSource(), infection.getDetectionSource()) &&
              ObjectsCompat.equals(getCreatedTimestamp(), infection.getCreatedTimestamp()) &&
              ObjectsCompat.equals(getDeletedTimestamp(), infection.getDeletedTimestamp());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getInfectedTimestamp())
      .append(getUserId())
      .append(getFromInfectionId())
      .append(getDetectionSource())
      .append(getCreatedTimestamp())
      .append(getDeletedTimestamp())
      .toString()
      .hashCode();
  }
  
  public static InfectedTimestampStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Infection justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Infection(
      id,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      infectedTimestamp,
      userId,
      fromInfectionId,
      detectionSource,
      createdTimestamp,
      deletedTimestamp);
  }
  public interface InfectedTimestampStep {
    DetectionSourceStep infectedTimestamp(Long infectedTimestamp);
  }
  

  public interface DetectionSourceStep {
    BuildStep detectionSource(DetectionSource detectionSource);
  }
  

  public interface BuildStep {
    Infection build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep userId(String userId);
    BuildStep fromInfectionId(String fromInfectionId);
    BuildStep createdTimestamp(Long createdTimestamp);
    BuildStep deletedTimestamp(Long deletedTimestamp);
  }
  

  public static class Builder implements InfectedTimestampStep, DetectionSourceStep, BuildStep {
    private String id;
    private Long infectedTimestamp;
    private DetectionSource detectionSource;
    private String userId;
    private String fromInfectionId;
    private Long createdTimestamp;
    private Long deletedTimestamp;
    @Override
     public Infection build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Infection(
          id,
          infectedTimestamp,
          userId,
          fromInfectionId,
          detectionSource,
          createdTimestamp,
          deletedTimestamp);
    }
    
    @Override
     public DetectionSourceStep infectedTimestamp(Long infectedTimestamp) {
        Objects.requireNonNull(infectedTimestamp);
        this.infectedTimestamp = infectedTimestamp;
        return this;
    }
    
    @Override
     public BuildStep detectionSource(DetectionSource detectionSource) {
        Objects.requireNonNull(detectionSource);
        this.detectionSource = detectionSource;
        return this;
    }
    
    @Override
     public BuildStep userId(String userId) {
        this.userId = userId;
        return this;
    }
    
    @Override
     public BuildStep fromInfectionId(String fromInfectionId) {
        this.fromInfectionId = fromInfectionId;
        return this;
    }
    
    @Override
     public BuildStep createdTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }
    
    @Override
     public BuildStep deletedTimestamp(Long deletedTimestamp) {
        this.deletedTimestamp = deletedTimestamp;
        return this;
    }
    
    /** 
     * WARNING: Do not set ID when creating a new object. Leave this blank and one will be auto generated for you.
     * This should only be set when referring to an already existing object.
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     * @throws IllegalArgumentException Checks that ID is in the proper format
     */
    public BuildStep id(String id) throws IllegalArgumentException {
        this.id = id;
        
        try {
            UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
        } catch (Exception exception) {
          throw new IllegalArgumentException("Model IDs must be unique in the format of UUID.",
                    exception);
        }
        
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, Long infectedTimestamp, String userId, String fromInfectionId, DetectionSource detectionSource, Long createdTimestamp, Long deletedTimestamp) {
      super.id(id);
      super.infectedTimestamp(infectedTimestamp)
        .detectionSource(detectionSource)
        .userId(userId)
        .fromInfectionId(fromInfectionId)
        .createdTimestamp(createdTimestamp)
        .deletedTimestamp(deletedTimestamp);
    }
    
    @Override
     public CopyOfBuilder infectedTimestamp(Long infectedTimestamp) {
      return (CopyOfBuilder) super.infectedTimestamp(infectedTimestamp);
    }
    
    @Override
     public CopyOfBuilder detectionSource(DetectionSource detectionSource) {
      return (CopyOfBuilder) super.detectionSource(detectionSource);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder fromInfectionId(String fromInfectionId) {
      return (CopyOfBuilder) super.fromInfectionId(fromInfectionId);
    }
    
    @Override
     public CopyOfBuilder createdTimestamp(Long createdTimestamp) {
      return (CopyOfBuilder) super.createdTimestamp(createdTimestamp);
    }
    
    @Override
     public CopyOfBuilder deletedTimestamp(Long deletedTimestamp) {
      return (CopyOfBuilder) super.deletedTimestamp(deletedTimestamp);
    }
  }
  
}
