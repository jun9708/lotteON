package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPdReviewImg is a Querydsl query type for PdReviewImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPdReviewImg extends EntityPathBase<PdReviewImg> {

    private static final long serialVersionUID = 1073184948L;

    public static final QPdReviewImg pdReviewImg = new QPdReviewImg("pdReviewImg");

    public final NumberPath<Integer> revImgNo = createNumber("revImgNo", Integer.class);

    public final NumberPath<Integer> revNo = createNumber("revNo", Integer.class);

    public final StringPath revThumb = createString("revThumb");

    public QPdReviewImg(String variable) {
        super(PdReviewImg.class, forVariable(variable));
    }

    public QPdReviewImg(Path<? extends PdReviewImg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPdReviewImg(PathMetadata metadata) {
        super(PdReviewImg.class, metadata);
    }

}

