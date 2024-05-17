package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProdQna is a Querydsl query type for ProdQna
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProdQna extends EntityPathBase<ProdQna> {

    private static final long serialVersionUID = 424968330L;

    public static final QProdQna prodQna = new QProdQna("prodQna");

    public final NumberPath<Integer> prodNo = createNumber("prodNo", Integer.class);

    public final StringPath prodQnaContent = createString("prodQnaContent");

    public final DateTimePath<java.time.LocalDateTime> prodQnaDate = createDateTime("prodQnaDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> prodQnaNo = createNumber("prodQnaNo", Integer.class);

    public final StringPath prodQnaSecret = createString("prodQnaSecret");

    public final StringPath prodQnaStatus = createString("prodQnaStatus");

    public final StringPath prodQnaTitle = createString("prodQnaTitle");

    public final StringPath userId = createString("userId");

    public QProdQna(String variable) {
        super(ProdQna.class, forVariable(variable));
    }

    public QProdQna(Path<? extends ProdQna> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProdQna(PathMetadata metadata) {
        super(ProdQna.class, metadata);
    }

}

