import { useNavigate } from 'react-router-dom';
import * as S from '../../style/Common/FooterStyle';

function Footer() {
  const navigate = useNavigate();
  // 챌린지 리스트페이지로 이동
  const LinkChallengePage = () => {
    navigate('/challengelist');
  };
  // 유저 리스트로 이동
  const LinkUserPage = () => {
    navigate('/userlist');
  };

  return (
    <S.FooterContainer>
      <S.Container>
        <S.Flex>
          <S.Logo>
            <div className="name">슬기로운 생활</div>
            <div className="text">
              행복한 삶을 살고 싶다면, 목표에 의지하라.
            </div>
          </S.Logo>

          <S.LinkContainer>
            <div onClick={LinkChallengePage}>Challenge</div>
            <div onClick={LinkUserPage}>Ranking</div>
          </S.LinkContainer>
          <S.LinkContainer>
            <div>About us</div>
            <div>Contact us</div>
            <div>Join us</div>
          </S.LinkContainer>
        </S.Flex>

        <S.DescriptionContainer>
          <div className="name">슬기로운 생활</div>
          <div className="icon">
            <S.GithubIcon />
            <S.InstagramIcon />
            <S.TwitterIcon />
            <S.LinkedinIcon />
          </div>
        </S.DescriptionContainer>
        <div className="text">
          &#40;주&#41; 슬기로운 생활 | 대표: 김민섭 김유현 오영운 한병주 김은비
          심이서
        </div>
      </S.Container>
    </S.FooterContainer>
  );
}

export default Footer;