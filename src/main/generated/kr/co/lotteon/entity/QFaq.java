package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFaq is a Querydsl query type for Faq
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFaq extends EntityPathBase<Faq> {

    private static final long serialVersionUID = -525137133L;

    public static final QFaq faq = new QFaq("faq");

    public final StringPath faqCate = createString("faqCate");

    public final StringPath faqContent = createString("faqContent");

    public final DateTimePath<java.time.LocalDateTime> faqDate = createDateTime("faqDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> faqHit = createNumber("faqHit", Integer.class);

    public final NumberPath<Integer> faqNo = createNumber("faqNo", Integer.class);

    public final StringPath faqTitle = createString("faqTitle");

    public final StringPath faqType = createString("faqType");

    public QFaq(String variable) {
        super(Faq.class, forVariable(variable));
    }

    public QFaq(Path<? extends Faq> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFaq(PathMetadata metadata) {
        super(Faq.class, metadata);
    }

}

