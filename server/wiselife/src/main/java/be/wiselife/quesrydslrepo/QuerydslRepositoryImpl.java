package be.wiselife.quesrydslrepo;

import be.wiselife.follow.entity.Follow;
import be.wiselife.image.entity.*;
import be.wiselife.member.entity.Member;
import be.wiselife.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static be.wiselife.follow.entity.QFollow.follow;
import static be.wiselife.image.entity.QMemberImage.*;
import static be.wiselife.image.entity.QReviewImage.*;
import static be.wiselife.image.entity.QChallengeRepImage.*;
import static be.wiselife.image.entity.QChallengeExamImage.*;
import static be.wiselife.image.entity.QChallengeCertImage.*;

import static be.wiselife.follow.entity.QFollow.follow;
import static be.wiselife.order.entity.QOrder.order;

@RequiredArgsConstructor
public class QuerydslRepositoryImpl implements QuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Follow findByFollowerIdAndFollowing(Long followerId, Member following) {
        return queryFactory
                .selectFrom(follow)
                .where(follow.followerId.eq(followerId)
                        .and(follow.following.eq(following)))
                .fetchOne();
    }

    @Override
    public MemberImage findByImageTypeAndMemberId(String imageType, Long memberId) {
        return queryFactory
                .selectFrom(memberImage)
                .where(memberImage.imageType.eq(imageType)
                        .and(memberImage.memberId.eq(memberId)))
                .fetchOne();
    }

    @Override
    public ReviewImage findByImageTypeAndReviewId(String imageType, Long reviewId) {
        return queryFactory
                .selectFrom(reviewImage)
                .where(reviewImage.imageType.eq(imageType)
                        .and(reviewImage.reviewId.eq(reviewId)))
                .fetchOne();
    }

    @Override
    public ChallengeRepImage findByImageTypeAndChallengeRep(String imageType, String randomIdForImage) {
        return queryFactory
                .selectFrom(challengeRepImage)
                .where(challengeRepImage.imageType.eq(imageType)
                        .and(challengeRepImage.randomIdForImage.eq(randomIdForImage)))
                .fetchOne();
    }
    @Override
    public List<ChallengeExamImage> findByImageTypeAndChallengeExam(String imageType,String randomIdForImage) {
        return queryFactory
                .selectFrom(challengeExamImage)
                .where(challengeExamImage.imageType.eq(imageType)
                        .and(challengeExamImage.randomIdForImage.eq(randomIdForImage)))
                .fetch();
    }
    @Override
    public ChallengeExamImage findByImageTypeAndImagePathAndChallengeExam(String imageType,String imagePath, String randomIdForImage) {
        return queryFactory
                .selectFrom(challengeExamImage)
                .where(challengeExamImage.imageType.eq(imageType)
                        .and(challengeExamImage.imagePath.eq(imagePath))
                        .and(challengeExamImage.randomIdForImage.eq(randomIdForImage)))
                .fetchOne();
    }

    @Override
    public List<ChallengeCertImage> findByImageTypeAndChallengeCertId(String imageType, Long challengeId) {
        return queryFactory
                .selectFrom(challengeCertImage)
                .where(challengeCertImage.imageType.eq(imageType)
                        .and(challengeCertImage.challengeId.eq(challengeId)))
                .fetch();
    }

    public List<Order> findByMemberId(Member member) { //성공한 결재내역만 보이게 출력
        return queryFactory
                .selectFrom(order)
                .where(order.member.eq(member)
                        .and(order.orderSuccess.isTrue()))
                .fetch();
    }
}
