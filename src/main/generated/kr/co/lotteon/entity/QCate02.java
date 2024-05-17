package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCate02 is a Querydsl query type for Cate02
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCate02 extends EntityPathBase<Cate02> {

    private static final long serialVersionUID = 2119830644L;

    public static final QCate02 cate02 = new QCate02("cate02");

    public final StringPath cate01No = createString("cate01No");

    public final StringPath cate02Name = createString("cate02Name");

    public final StringPath cate02No = createString("cate02No");

    public QCate02(String variable) {
        super(Cate02.class, forVariable(variable));
    }

    public QCate02(Path<? extends Cate02> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCate02(PathMetadata metadata) {
        super(Cate02.class, metadata);
    }

}

