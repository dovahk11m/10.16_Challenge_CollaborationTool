package choong.domain.member.MemberSignup;

import java.util.List;
import java.util.Random;

public class NicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "행복한", "즐거운", "빛나는", "용감한", "친절한", "똑똑한", "신비한", "고요한", "강력한", "우아한"
    );

    private static final List<String> NOUNS = List.of(
            "사자", "호랑이", "고양이", "강아지", "돌고래", "코끼리", "기린", "다람쥐", "부엉이", "펭귄"
    );

    private static final Random RANDOM = new Random();

    public static String generate() {

        String adjective = ADJECTIVES.get(RANDOM.nextInt(ADJECTIVES.size()));
        String noun = NOUNS.get(RANDOM.nextInt(NOUNS.size()));
        int number = RANDOM.nextInt(900) + 100; // 100 ~ 999

        return adjective + " " + noun + " " + number;
    }
}