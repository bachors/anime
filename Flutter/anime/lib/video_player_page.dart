import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'models/anime_model.dart';

class VideoPlayerPage extends StatefulWidget {
  final String episodeSlug;
  
  const VideoPlayerPage({super.key, required this.episodeSlug});

  @override
  State<VideoPlayerPage> createState() => _VideoPlayerPageState();
}

class _VideoPlayerPageState extends State<VideoPlayerPage> {
  EpisodeDetail? _episodeDetail;
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadEpisodeDetail();
  }

  Future<void> _loadEpisodeDetail() async {
    try {
      final response = await http.get(
        Uri.parse('https://www.sankavollerei.com/anime/episode/${widget.episodeSlug}'),
      );
      
      if (response.statusCode == 200) {
        final data = json.decode(response.body);
        final episodeResponse = EpisodeDetailResponse.fromJson(data);
        setState(() {
          _episodeDetail = episodeResponse.data;
        });
      }
    } catch (e) {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Error: $e')),
      );
    } finally {
      setState(() {
        _isLoading = false;
      });
    }
  }

  void _copyUrl() {
    if (_episodeDetail?.streamUrl != null) {
      Clipboard.setData(ClipboardData(text: _episodeDetail!.streamUrl));
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Video URL copied to clipboard')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(_episodeDetail?.episode ?? 'Video Player'),
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : _episodeDetail == null
              ? const Center(child: Text('Failed to load video'))
              : Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(Icons.play_circle_fill, size: 100, color: Colors.blue),
                      const SizedBox(height: 20),
                      Text(
                        _episodeDetail!.episode,
                        style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 20),
                      Container(
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          border: Border.all(color: Colors.grey),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Streaming URL:',
                              style: TextStyle(fontWeight: FontWeight.bold),
                            ),
                            const SizedBox(height: 8),
                            SelectableText(
                              _episodeDetail!.streamUrl,
                              style: const TextStyle(fontSize: 12),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(height: 20),
                      ElevatedButton.icon(
                        onPressed: _copyUrl,
                        icon: const Icon(Icons.copy),
                        label: const Text('Copy URL'),
                        style: ElevatedButton.styleFrom(
                          padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 15),
                        ),
                      ),
                      const SizedBox(height: 10),
                      const Text(
                        'Copy the URL and paste it in your browser to watch the video',
                        style: TextStyle(fontSize: 12, color: Colors.grey),
                        textAlign: TextAlign.center,
                      ),
                    ],
                  ),
                ),
    );
  }
}