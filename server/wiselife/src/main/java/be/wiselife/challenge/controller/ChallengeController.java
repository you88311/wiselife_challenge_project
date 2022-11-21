package be.wiselife.challenge.controller;

import be.wiselife.challenge.dto.ChallengeDto;
import be.wiselife.challenge.entity.Challenge;
import be.wiselife.challenge.mapper.ChallengeMapper;
import be.wiselife.challenge.service.ChallengeService;
import be.wiselife.challengetalk.mapper.ChallengeTalkMapper;
import be.wiselife.dto.SingleResponseDto;
import be.wiselife.member.entity.Member;
import be.wiselife.member.service.MemberService;
import be.wiselife.security.JwtTokenizer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/challenges")
@Validated
public class ChallengeController {
    private final ChallengeMapper challengeMapper;
    private final ChallengeService challengeService;
    private final ChallengeTalkMapper challengeTalkMapper;
    private final MemberService memberService;

    public ChallengeController(ChallengeMapper challengeMapper, ChallengeService challengeService, ChallengeTalkMapper challengeTalkMapper, MemberService memberService) {
        this.challengeMapper = challengeMapper;
        this.challengeService = challengeService;
        this.challengeTalkMapper = challengeTalkMapper;
        this.memberService = memberService;
    }

    /**
     * 챌린지 생성
     */
    @PostMapping
    public ResponseEntity postChallenge(@Valid @RequestBody ChallengeDto.Post challengePostDto,
                                        HttpServletRequest request) {

        Challenge challenge = challengeMapper.challengePostDtoToChallenge(challengePostDto);
        challenge = challengeService.createChallenge(challenge, memberService.getLoginMember(request));

        return new ResponseEntity<>(
                new SingleResponseDto<>(challengeMapper.challengeToChallengeSimpleResponseDto(challenge))
                , HttpStatus.CREATED);
    }

    /**
     * 챌린지 수정
     */
    @PatchMapping
    public ResponseEntity patchChallenge(@Valid @RequestBody ChallengeDto.Patch challengePatchDto,
                                         HttpServletRequest request) {

        Challenge challenge = challengeMapper.challengePatchDtoToChallenge(challengePatchDto);
        challenge = challengeService.updateChallenge(challenge, memberService.getLoginMember(request));

        return new ResponseEntity<>(
                new SingleResponseDto<>(challengeMapper.challengeToChallengeSimpleResponseDto(challenge))
                , HttpStatus.OK);
    }

    @PostMapping("/participate/{challengeId}")
    public ResponseEntity postMemberAndChallenge(@PathVariable("challengeId") @Positive Long challengeId,
                                                 HttpServletRequest request) {

        Challenge challengeFromRepository = challengeService.findChallengeById(challengeId);

        Challenge challenge = challengeService.participateChallenge(challengeFromRepository, memberService.getLoginMember(request));
        return new ResponseEntity<>(
                new SingleResponseDto<>(challengeMapper.
                        challengeToChallengeDetailResponseDto(challenge, challengeTalkMapper, memberService)),
                HttpStatus.CREATED);
    }

    /**
     * 작성자 : 유현
     * 인증사진 등록
     *
     * @param cert    인증사진이 속한 Challenge 아이디와 인증사진 경로
     * @param request 로그인한 사람의 이메일 정보를 가져오기위한 인자값
     *                TODO :
     *                챌린지 참여인원인지 판단하는 로직 추가
     *                응답값을 "/challenges/{challenge-id}으로 리다이렉션되게 개선 필요
     */

    @PostMapping("/cert")
    public ResponseEntity postMemberCertification(@Valid @RequestBody ChallengeDto.Cert cert,
                                                  HttpServletRequest request) {
        Challenge certImageInfo = challengeMapper.certDtoToChallenge(cert);

        Challenge challenge = challengeService.createCertImage(certImageInfo, memberService.getLoginMember(request));

        return new ResponseEntity<>(
                new SingleResponseDto<>(challengeMapper.challengeToChallengeSimpleResponseDto(challenge)), HttpStatus.CREATED);
    }

    //작성자 : 유현
    @PatchMapping("/cert")
    public ResponseEntity patchMemberCertification(@Valid @RequestBody ChallengeDto.Cert cert,
                                                   HttpServletRequest request) {

        Challenge certImageInfo = challengeMapper.certDtoToChallenge(cert);

        Challenge challenge = challengeService.updateCertImage(certImageInfo, memberService.getLoginMember(request));

        return new ResponseEntity<>(
                new SingleResponseDto<>(challengeMapper.challengeToChallengeSimpleResponseDto(challenge)), HttpStatus.CREATED);
    }


    /**
     * 챌린지 상세페이지 조회
     * TODO:
     *      1) 만약 유저가 해당 챌린지 참여중이라면 별도로 유저의 해당 챌린지 성공률도 표시함
     *      2) 챌린지 참여중인 유저들의 평균 챌린지 성공률
     *      3) 동일한 사용자의 조회수 중복 증가 방지 기능
     */
    @GetMapping("/{challenge-id}")
    public ResponseEntity getChallenge(@PathVariable("challenge-id") @Positive Long challengeId) {

        Challenge challenge = challengeService.getChallenge(challengeId); //챌린지 찾기
        challenge = challengeService.updateViewCount(challenge); //조회수 증가

        ChallengeDto.DetailResponse challengeResponseDto = challengeMapper.challengeToChallengeDetailResponseDto(challenge, challengeTalkMapper, memberService);

        return new ResponseEntity<>(
                new SingleResponseDto<>(challengeResponseDto)
                , HttpStatus.OK);
    }

    /**
     * 작성자 : 유현
     * 챌린지 상세페이지 조회(팀원들하고 상의해야하는 부분)
     * 로그인 된 유저가 아닐시 인증사진은 안나오게 simpleResponse로 응답을 준다.
     * 로그인 된 유저면 자신이 인증한 사진만 볼 수 있게 detailResponse를 응답해 준다.
     */
    @GetMapping("/test/{challenge-id}")
    public ResponseEntity getChallengeV1(@PathVariable("challenge-id") @Positive Long challengeId,
                                         HttpServletRequest request) {
        Challenge challenge = challengeService.findChallengeById(challengeId);
        challenge = challengeService.updateViewCount(challenge);
        if (request.getHeader("Authorization") == null) {
            challenge.setChallengeCertImagePath("");
            //TODO: simpleResponseDto로 변경 필요
            ChallengeDto.DetailResponse challengeResponseDto
                    = challengeMapper.challengeToChallengeDetailResponseDto(challenge, challengeTalkMapper, memberService);
            return new ResponseEntity<>(
                    new SingleResponseDto<>(challengeResponseDto), HttpStatus.OK);
        } else {
            challenge = challengeService.getCertification(challenge, memberService.getLoginMember(request));

            ChallengeDto.DetailResponse challengeResponseDto
                    = challengeMapper.challengeToChallengeDetailResponseDto(challenge, challengeTalkMapper, memberService);
            return new ResponseEntity<>(
                    new SingleResponseDto<>(challengeResponseDto), HttpStatus.OK);
        }
    }

    /**
     * 챌린지 삭제
     * TODO: 챌린지가 시작했다면 챌린지 작성자라도 수정 불가능
     */
    @DeleteMapping({"/{challenge-id}"})
    public ResponseEntity deleteChallenge(@PathVariable("challenge-id") @Positive Long challengeId,
                                          HttpServletRequest request) {

        challengeService.deleteChallenge(challengeId, memberService.getLoginMember(request));

        return new ResponseEntity<>(
                "Challenge 삭제 완료", HttpStatus.NO_CONTENT);
    }

    /**
     * JWT 토큰의 유저 이메일 이용해 login된 멤버 객체 가져오는 함수
     * 시도하려는 유저에게 권한이 있는지 확인하기 위해 사용한다.
     */

}
