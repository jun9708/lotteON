package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QQna is a Querydsl query type for Qna
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQna extends EntityPathBase<Qna> {

    private static final long serialVersionUID = -525126175L;

    public static final QQna qna = new QQna("qna");

    public final StringPath qnaCate = createString("qnaCate");

    public final StringPath qnaContent = createString("qnaContent");

    public final DateTimePath<java.time.LocalDateTime> qnaDate = createDateTime("qnaDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> qnaNo = createNumber("qnaNo", Integer.class);

    public final StringPath qnaReply = createString("qnaReply");

    public final StringPath qnaStatus = createString("qnaStatus");

    public final StringPath qnaTitle = createString("qnaTitle");

    public final StringPath qnaType = createString("qnaType");

    public final StringPath userId = createString("userId");

    public QQna(String variable) {
        super(Qna.class, forVariable(variable));
    }

    public QQna(Path<? extends Qna> path) {
        super(path.getType(), path.getMetadata());
    }

    public QQna(PathMetadata metadata) {
        super(Qna.class, metadata);
    }

}

