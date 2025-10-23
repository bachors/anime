class AnimeSearchResponse {
  final String status;
  final List<AnimeResult> searchResults;

  AnimeSearchResponse({required this.status, required this.searchResults});

  factory AnimeSearchResponse.fromJson(Map<String, dynamic> json) {
    return AnimeSearchResponse(
      status: json['status'] ?? '',
      searchResults: (json['search_results'] as List?)
          ?.map((item) => AnimeResult.fromJson(item))
          .toList() ?? [],
    );
  }
}

class AnimeResult {
  final String title;
  final String poster;
  final String rating;
  final String type;
  final String status;
  final String synopsis;
  final String slug;

  AnimeResult({
    required this.title,
    required this.poster,
    required this.rating,
    required this.type,
    required this.status,
    required this.synopsis,
    required this.slug,
  });

  factory AnimeResult.fromJson(Map<String, dynamic> json) {
    return AnimeResult(
      title: json['title'] ?? '',
      poster: json['poster'] ?? '',
      rating: json['rating'] ?? '',
      type: json['type'] ?? '',
      status: json['status'] ?? '',
      synopsis: json['synopsis'] ?? '',
      slug: json['slug'] ?? '',
    );
  }
}

class HomeResponse {
  final String status;
  final HomeData data;

  HomeResponse({required this.status, required this.data});

  factory HomeResponse.fromJson(Map<String, dynamic> json) {
    return HomeResponse(
      status: json['status'] ?? '',
      data: HomeData.fromJson(json['data'] ?? {}),
    );
  }
}

class HomeData {
  final List<OngoingAnime> ongoingAnime;
  final List<CompleteAnime> completeAnime;

  HomeData({required this.ongoingAnime, required this.completeAnime});

  factory HomeData.fromJson(Map<String, dynamic> json) {
    return HomeData(
      ongoingAnime: (json['ongoing_anime'] as List?)
          ?.map((item) => OngoingAnime.fromJson(item))
          .toList() ?? [],
      completeAnime: (json['complete_anime'] as List?)
          ?.map((item) => CompleteAnime.fromJson(item))
          .toList() ?? [],
    );
  }
}

class OngoingAnime {
  final String title;
  final String slug;
  final String poster;
  final String currentEpisode;
  final String releaseDay;

  OngoingAnime({
    required this.title,
    required this.slug,
    required this.poster,
    required this.currentEpisode,
    required this.releaseDay,
  });

  factory OngoingAnime.fromJson(Map<String, dynamic> json) {
    return OngoingAnime(
      title: json['title'] ?? '',
      slug: json['slug'] ?? '',
      poster: json['poster'] ?? '',
      currentEpisode: json['current_episode'] ?? '',
      releaseDay: json['release_day'] ?? '',
    );
  }
}

class CompleteAnime {
  final String title;
  final String slug;
  final String poster;
  final String episodeCount;
  final String rating;

  CompleteAnime({
    required this.title,
    required this.slug,
    required this.poster,
    required this.episodeCount,
    required this.rating,
  });

  factory CompleteAnime.fromJson(Map<String, dynamic> json) {
    return CompleteAnime(
      title: json['title'] ?? '',
      slug: json['slug'] ?? '',
      poster: json['poster'] ?? '',
      episodeCount: json['episode_count'] ?? '',
      rating: json['rating'] ?? '',
    );
  }
}

class AnimeDetailResponse {
  final String status;
  final AnimeDetail data;

  AnimeDetailResponse({required this.status, required this.data});

  factory AnimeDetailResponse.fromJson(Map<String, dynamic> json) {
    return AnimeDetailResponse(
      status: json['status'] ?? '',
      data: AnimeDetail.fromJson(json['data'] ?? {}),
    );
  }
}

class AnimeDetail {
  final String title;
  final String poster;
  final String rating;
  final String type;
  final String status;
  final String episodeCount;
  final String synopsis;
  final List<Genre> genres;
  final List<Episode> episodeLists;

  AnimeDetail({
    required this.title,
    required this.poster,
    required this.rating,
    required this.type,
    required this.status,
    required this.episodeCount,
    required this.synopsis,
    required this.genres,
    required this.episodeLists,
  });

  factory AnimeDetail.fromJson(Map<String, dynamic> json) {
    return AnimeDetail(
      title: json['title'] ?? '',
      poster: json['poster'] ?? '',
      rating: json['rating'] ?? '',
      type: json['type'] ?? '',
      status: json['status'] ?? '',
      episodeCount: json['episode_count'] ?? '',
      synopsis: json['synopsis'] ?? '',
      genres: (json['genres'] as List?)
          ?.map((item) => Genre.fromJson(item))
          .toList() ?? [],
      episodeLists: (json['episode_lists'] as List?)
          ?.map((item) => Episode.fromJson(item))
          .toList() ?? [],
    );
  }
}

class Genre {
  final String name;

  Genre({required this.name});

  factory Genre.fromJson(Map<String, dynamic> json) {
    return Genre(name: json['name'] ?? '');
  }
}

class Episode {
  final String episode;
  final int episodeNumber;
  final String slug;

  Episode({required this.episode, required this.episodeNumber, required this.slug});

  factory Episode.fromJson(Map<String, dynamic> json) {
    return Episode(
      episode: json['episode'] ?? '',
      episodeNumber: json['episode_number'] ?? 0,
      slug: json['slug'] ?? '',
    );
  }
}

class EpisodeDetailResponse {
  final String status;
  final EpisodeDetail data;

  EpisodeDetailResponse({required this.status, required this.data});

  factory EpisodeDetailResponse.fromJson(Map<String, dynamic> json) {
    return EpisodeDetailResponse(
      status: json['status'] ?? '',
      data: EpisodeDetail.fromJson(json['data'] ?? {}),
    );
  }
}

class EpisodeDetail {
  final String episode;
  final String streamUrl;

  EpisodeDetail({required this.episode, required this.streamUrl});

  factory EpisodeDetail.fromJson(Map<String, dynamic> json) {
    return EpisodeDetail(
      episode: json['episode'] ?? '',
      streamUrl: json['stream_url'] ?? '',
    );
  }
}