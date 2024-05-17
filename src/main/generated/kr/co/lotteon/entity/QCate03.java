package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCate03 is a Querydsl query type for Cate03
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCate03 extends EntityPathBase<Cate03> {

    private static final long serialVersionUID = 2119830645L;

    public static final QCate03 cate03 = new QCate03("cate03");

    public final StringPath cate02No = createString("cate02No");

    public final StringPath cate03Name = createString("cate03Name");

    public final StringPath cate03No = createString("cate03No");

    public QCate03(String variable) {
        super(Cate03.class, forVariable(variable));
    }

    public QCate03(Path<? extends Cate03> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCate03(PathMetadata metadata) {
        super(Cate03.class, metadata);
    }

}

