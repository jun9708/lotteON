package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserPoint is a Querydsl query type for UserPoint
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPoint extends EntityPathBase<UserPoint> {

    private static final long serialVersionUID = 1782968002L;

    public static final QUserPoint userPoint = new QUserPoint("userPoint");

    public final NumberPath<Integer> pointBalance = createNumber("pointBalance", Integer.class);

    public final NumberPath<Integer> pointNo = createNumber("pointNo", Integer.class);

    public final StringPath userId = createString("userId");

    public QUserPoint(String variable) {
        super(UserPoint.class, forVariable(variable));
    }

    public QUserPoint(Path<? extends UserPoint> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserPoint(PathMetadata metadata) {
        super(UserPoint.class, metadata);
    }

}

