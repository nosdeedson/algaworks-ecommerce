package com.ejs.util.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanToSimNaoConverter implements AttributeConverter<Boolean, String> {

	/*
	 * O primeiro tipo de atributo (Boolean) é o tipo do valor da classe java
	 * O segundo (String) é o tipo a ser salvo no banco de dados
	 */
	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		return Boolean.TRUE.equals(attribute) ? "Sim" : "Não";
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return "Sim".equals(dbData) ? Boolean.TRUE: Boolean.FALSE;
	}

}
