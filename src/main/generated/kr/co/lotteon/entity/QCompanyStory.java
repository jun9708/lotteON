package kr.co.lotteon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCompanyStory is a Querydsl query type for CompanyStory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCompanyStory extends EntityPathBase<CompanyStory> {

    private static final long serialVersionUID = -2034876645L;

    public static final QCompanyStory companyStory = new QCompanyStory("companyStory");

    public final StringPath storyCate = createString("storyCate");

    public final StringPath storyContent = createString("storyContent");

    public final DatePath<java.time.LocalDate> storyDate = createDate("storyDate", java.time.LocalDate.class);

    public final StringPath storyImg = createString("storyImg");

    public final StringPath storyName = createString("storyName");

    public final NumberPath<Integer> storyNo = createNumber("storyNo", Integer.class);

    public QCompanyStory(String variable) {
        super(CompanyStory.class, forVariable(variable));
    }

    public QCompanyStory(Path<? extends CompanyStory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyStory(PathMetadata metadata) {
        super(CompanyStory.class, metadata);
    }

}

