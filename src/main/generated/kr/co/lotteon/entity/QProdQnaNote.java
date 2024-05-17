package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProdQnaNote is a Querydsl query type for ProdQnaNote
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProdQnaNote extends EntityPathBase<ProdQnaNote> {

    private static final long serialVersionUID = 1657950108L;

    public static final QProdQnaNote prodQnaNote = new QProdQnaNote("prodQnaNote");

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> cQnaDate = createDateTime("cQnaDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> cQnaNo = createNumber("cQnaNo", Integer.class);

    public final NumberPath<Integer> prodQnaNo = createNumber("prodQnaNo", Integer.class);

    public final StringPath sellerNo = createString("sellerNo");

    public QProdQnaNote(String variable) {
        super(ProdQnaNote.class, forVariable(variable));
    }

    public QProdQnaNote(Path<? extends ProdQnaNote> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProdQnaNote(PathMetadata metadata) {
        super(ProdQnaNote.class, metadata);
    }

}

