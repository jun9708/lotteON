package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPdReview is a Querydsl query type for PdReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPdReview extends EntityPathBase<PdReview> {

    private static final long serialVersionUID = -79257457L;

    public static final QPdReview pdReview = new QPdReview("pdReview");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> revAddDate = createDateTime("revAddDate", java.time.LocalDateTime.class);

    public final StringPath revContent = createString("revContent");

    public final NumberPath<Integer> revNo = createNumber("revNo", Integer.class);

    public final NumberPath<Integer> revScore = createNumber("revScore", Integer.class);

    public final StringPath userId = createString("userId");

    public QPdReview(String variable) {
        super(PdReview.class, forVariable(variable));
    }

    public QPdReview(Path<? extends PdReview> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPdReview(PathMetadata metadata) {
        super(PdReview.class, metadata);
    }

}

