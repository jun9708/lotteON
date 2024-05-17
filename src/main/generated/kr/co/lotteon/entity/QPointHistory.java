package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPointHistory is a Querydsl query type for PointHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPointHistory extends EntityPathBase<PointHistory> {

    private static final long serialVersionUID = -1291062905L;

    public static final QPointHistory pointHistory = new QPointHistory("pointHistory");

    public final StringPath changeCode = createString("changeCode");

    public final DateTimePath<java.time.LocalDateTime> changeDate = createDateTime("changeDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> changePoint = createNumber("changePoint", Integer.class);

    public final StringPath changeType = createString("changeType");

    public final NumberPath<Integer> pointHisNo = createNumber("pointHisNo", Integer.class);

    public final NumberPath<Integer> pointNo = createNumber("pointNo", Integer.class);

    public QPointHistory(String variable) {
        super(PointHistory.class, forVariable(variable));
    }

    public QPointHistory(Path<? extends PointHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPointHistory(PathMetadata metadata) {
        super(PointHistory.class, metadata);
    }

}

