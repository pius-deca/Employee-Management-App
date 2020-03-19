package com.example.demo.service;

import com.example.demo.model.DatabaseSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import java.util.Objects;

@Service
public class SequenceGeneratorService {

    private MongoOperations operations;

    @Autowired
    public SequenceGeneratorService(MongoOperations operations) {
        this.operations = operations;
    }

    public long generateSequence(String seqName){
        DatabaseSequence counter = operations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
