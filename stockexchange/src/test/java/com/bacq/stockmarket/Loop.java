package com.bacq.stockmarket;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Loop implements TestRule {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Repeat {
        int times();
    }

    private static class RepeatStatement extends Statement {

        private final int times;

        private final Statement statement;

        public RepeatStatement(int times, Statement statement){
            this.times = times;
            this.statement = statement;
        }

        @Override
        public void evaluate() throws Throwable {
            for(int i=0;i<times;i++){
                statement.evaluate();
            }
        }
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        Statement result = statement;
        Repeat repeat = description.getAnnotation(Repeat.class);
        if(repeat != null){
            int times = repeat.times();
            result = new RepeatStatement(times, statement);
        }
        return result;
    }
}
