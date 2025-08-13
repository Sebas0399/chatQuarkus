package db.utils;

import io.vertx.core.json.JsonObject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)

public class JsonObjectConverter implements AttributeConverter<JsonObject, String> {
	@Override
	public String convertToDatabaseColumn(JsonObject jsonObject) {
		return jsonObject != null ? jsonObject.encode() : null;
	}

	@Override
	public JsonObject convertToEntityAttribute(String s) {
		return s != null ? new JsonObject(s) : null;
	}
}
