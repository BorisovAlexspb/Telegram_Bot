package edu.java.dto.github;

public enum EventType {
    PUSH {

        public String generateUpdateMessage(EventResponse.Payload payload) {
            StringBuilder stringBuilder = new StringBuilder("Обновление: произошёл push в репозиторий\n");
            stringBuilder.append("Подробности:\nCommits:\n");
            for (int i = 0; i < payload.commits().size(); i++) {
                stringBuilder.append("\tCommit %d: %s\n".formatted(i + 1, payload.commits().get(i).message()));
            }
            return stringBuilder.toString();
        }
    },
    UNKNOWN {
        public String generateUpdateMessage(EventResponse.Payload payload) {
            return "В репозитории произошло обновление";
        }
    };
}
