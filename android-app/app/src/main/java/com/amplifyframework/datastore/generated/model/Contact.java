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

/** This is an auto generated class representing the Contact type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Contacts")
@Index(name = "undefined", fields = {"userId"})
public final class Contact implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField USER_ID = field("userId");
  public static final QueryField CONTACT_USER_ID = field("contactUserId");
  public static final QueryField CONTACT_TIMESTAMP = field("contactTimestamp");
  public static final QueryField CREATED_TIMESTAMP = field("createdTimestamp");
  public static final QueryField EXPIRATION_TIMESTAMP = field("expirationTimestamp");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String userId;
  private final @ModelField(targetType="ID", isRequired = true) String contactUserId;
  private final @ModelField(targetType="AWSTimestamp", isRequired = true) Long contactTimestamp;
  private final @ModelField(targetType="AWSTimestamp") Long createdTimestamp;
  private final @ModelField(targetType="AWSTimestamp") Long expirationTimestamp;
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return userId;
  }
  
  public String getContactUserId() {
      return contactUserId;
  }
  
  public Long getContactTimestamp() {
      return contactTimestamp;
  }
  
  public Long getCreatedTimestamp() {
      return createdTimestamp;
  }
  
  public Long getExpirationTimestamp() {
      return expirationTimestamp;
  }
  
  private Contact(String id, String userId, String contactUserId, Long contactTimestamp, Long createdTimestamp, Long expirationTimestamp) {
    this.id = id;
    this.userId = userId;
    this.contactUserId = contactUserId;
    this.contactTimestamp = contactTimestamp;
    this.createdTimestamp = createdTimestamp;
    this.expirationTimestamp = expirationTimestamp;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Contact contact = (Contact) obj;
      return ObjectsCompat.equals(getId(), contact.getId()) &&
              ObjectsCompat.equals(getUserId(), contact.getUserId()) &&
              ObjectsCompat.equals(getContactUserId(), contact.getContactUserId()) &&
              ObjectsCompat.equals(getContactTimestamp(), contact.getContactTimestamp()) &&
              ObjectsCompat.equals(getCreatedTimestamp(), contact.getCreatedTimestamp()) &&
              ObjectsCompat.equals(getExpirationTimestamp(), contact.getExpirationTimestamp());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getContactUserId())
      .append(getContactTimestamp())
      .append(getCreatedTimestamp())
      .append(getExpirationTimestamp())
      .toString()
      .hashCode();
  }
  
  public static UserIdStep builder() {
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
  public static Contact justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Contact(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      userId,
      contactUserId,
      contactTimestamp,
      createdTimestamp,
      expirationTimestamp);
  }
  public interface UserIdStep {
    ContactUserIdStep userId(String userId);
  }
  

  public interface ContactUserIdStep {
    ContactTimestampStep contactUserId(String contactUserId);
  }
  

  public interface ContactTimestampStep {
    BuildStep contactTimestamp(Long contactTimestamp);
  }
  

  public interface BuildStep {
    Contact build();
    BuildStep id(String id) throws IllegalArgumentException;
    BuildStep createdTimestamp(Long createdTimestamp);
    BuildStep expirationTimestamp(Long expirationTimestamp);
  }
  

  public static class Builder implements UserIdStep, ContactUserIdStep, ContactTimestampStep, BuildStep {
    private String id;
    private String userId;
    private String contactUserId;
    private Long contactTimestamp;
    private Long createdTimestamp;
    private Long expirationTimestamp;
    @Override
     public Contact build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Contact(
          id,
          userId,
          contactUserId,
          contactTimestamp,
          createdTimestamp,
          expirationTimestamp);
    }
    
    @Override
     public ContactUserIdStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.userId = userId;
        return this;
    }
    
    @Override
     public ContactTimestampStep contactUserId(String contactUserId) {
        Objects.requireNonNull(contactUserId);
        this.contactUserId = contactUserId;
        return this;
    }
    
    @Override
     public BuildStep contactTimestamp(Long contactTimestamp) {
        Objects.requireNonNull(contactTimestamp);
        this.contactTimestamp = contactTimestamp;
        return this;
    }
    
    @Override
     public BuildStep createdTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
        return this;
    }
    
    @Override
     public BuildStep expirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
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
    private CopyOfBuilder(String id, String userId, String contactUserId, Long contactTimestamp, Long createdTimestamp, Long expirationTimestamp) {
      super.id(id);
      super.userId(userId)
        .contactUserId(contactUserId)
        .contactTimestamp(contactTimestamp)
        .createdTimestamp(createdTimestamp)
        .expirationTimestamp(expirationTimestamp);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder contactUserId(String contactUserId) {
      return (CopyOfBuilder) super.contactUserId(contactUserId);
    }
    
    @Override
     public CopyOfBuilder contactTimestamp(Long contactTimestamp) {
      return (CopyOfBuilder) super.contactTimestamp(contactTimestamp);
    }
    
    @Override
     public CopyOfBuilder createdTimestamp(Long createdTimestamp) {
      return (CopyOfBuilder) super.createdTimestamp(createdTimestamp);
    }
    
    @Override
     public CopyOfBuilder expirationTimestamp(Long expirationTimestamp) {
      return (CopyOfBuilder) super.expirationTimestamp(expirationTimestamp);
    }
  }
  
}
