package org.tic.vertx.examples.web.request.enums;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;
import io.vertx.ext.web.api.validation.CustomValidator;
import io.vertx.ext.web.api.validation.ValidationException;

import java.util.Set;

public enum JsonBodyValidations {

    ORDER_ARRAY {
        @Override
        public CustomValidator validator() {
            return rc -> validate(JsonBodySchema.ORDER_ARRAY_SCHEMA, rc.getBodyAsString());
        }
    };

    public void validate(String jsonSchema, String json) {
        try {
            JsonSchemaFactory factory = new JsonSchemaFactory();
            JsonSchema schema = factory.getSchema(jsonSchema);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            Set<ValidationMessage> errors = schema.validate(node);
            if (!errors.isEmpty()) {
                throw new ValidationException(errors.iterator().next().getMessage());
            }
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    public abstract CustomValidator validator();
}
