package nextstep.favorite.application;

import nextstep.auth.domain.LoginMember;
import nextstep.favorite.application.dto.FavoriteRequest;
import nextstep.favorite.application.dto.FavoriteResponse;
import nextstep.favorite.domain.Favorite;
import nextstep.favorite.domain.FavoriteRepository;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.section.domain.Section;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final LineService lineService;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository, LineService lineService) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.lineService = lineService;
        this.memberRepository = memberRepository;
    }

    public Long createFavorite(LoginMember loginMember, FavoriteRequest request) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(IllegalArgumentException::new);
        Station sourceStation = stationRepository.findById(request.getSource()).orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findById(request.getTarget()).orElseThrow(IllegalArgumentException::new);
        List<Section> sectionList = lineService.getSectionList();

        Favorite favorite = new Favorite(member.getId(), sourceStation, targetStation, sectionList);
        favoriteRepository.save(favorite);
        return favorite.getId();
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberRepository.findByEmail(loginMember.getEmail()).orElseThrow(IllegalArgumentException::new);
        List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
        return favorites.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
