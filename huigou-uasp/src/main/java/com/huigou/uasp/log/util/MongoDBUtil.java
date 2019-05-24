package com.huigou.uasp.log.util;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.huigou.uasp.log.util.domain.model.Sequence;

public class MongoDBUtil {
	private static final String SEQUENCE = "sequence";
	
	public static final String DESC = "desc";

	public static Long getNextSequence(MongoOperations mongoOperations) {
		Query query = new Query();
		Update update = new Update();
		update.inc(SEQUENCE, 1);
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.upsert(true);
		options.returnNew(true);
		Sequence sequence = mongoOperations.findAndModify(query, update, Sequence.class);
		return sequence.getSequence();
	}
}
