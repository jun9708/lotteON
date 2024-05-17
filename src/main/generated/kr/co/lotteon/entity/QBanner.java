package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBanner is a Querydsl query type for Banner
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBanner extends EntityPathBase<Banner> {

    private static final long serialVersionUID = 2091033103L;

    public static final QBanner banner = new QBanner("banner");

    public final DatePath<java.time.LocalDate> banEdate = createDate("banEdate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> banEtime = createTime("banEtime", java.time.LocalTime.class);

    public final StringPath banImgCate = createString("banImgCate");

    public final StringPath banImgName = createString("banImgName");

    public final StringPath banLink = createString("banLink");

    public final StringPath banName = createString("banName");

    public final NumberPath<Integer> banNo = createNumber("banNo", Integer.class);

    public final DatePath<java.time.LocalDate> banSdate = createDate("banSdate", java.time.LocalDate.class);

    public final TimePath<java.time.LocalTime> banStime = createTime("banStime", java.time.LocalTime.class);

    public final StringPath banUsable = createString("banUsable");

    public QBanner(String variable) {
        super(Banner.class, forVariable(variable));
    }

    public QBanner(Path<? extends Banner> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBanner(PathMetadata metadata) {
        super(Banner.class, metadata);
    }

}

