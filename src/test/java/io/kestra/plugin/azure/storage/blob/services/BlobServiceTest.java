package io.kestra.plugin.azure.storage.blob.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BlobServiceTest {

    @Test
    void shouldPreserveSubdirectoriesWhenMovingUnderPrefix() {
        var dest1 = BlobService.destinationName("archive/", "incoming/2023/report.csv", "incoming/");
        var dest2 = BlobService.destinationName("archive/", "incoming/2024/report.csv", "incoming/");

        assertThat(dest1).isEqualTo("archive/2023/report.csv");
        assertThat(dest2).isEqualTo("archive/2024/report.csv");
        assertThat(dest1).isNotEqualTo(dest2);
    }

    @Test
    void shouldPreserveSubdirectoriesWhenPrefixDoesNotEndOnFolderBoundary() {
        var dest1 = BlobService.destinationName("archive/", "data/2023-01/report.csv", "data/2023");
        var dest2 = BlobService.destinationName("archive/", "data/2023-02/report.csv", "data/2023");

        assertThat(dest1).isEqualTo("archive/2023-01/report.csv");
        assertThat(dest2).isEqualTo("archive/2023-02/report.csv");
        assertThat(dest1).isNotEqualTo(dest2);
    }

    @Test
    void shouldPreserveDeepSubdirectoriesWithPrefixWithoutTrailingSlash() {
        var dest = BlobService.destinationName("archive", "incoming/nested/deep/report.csv", "incoming");
        assertThat(dest).isEqualTo("archive/nested/deep/report.csv");
    }

    @Test
    void shouldPreserveFilenameForFlatFileUnderPrefix() {
        var dest = BlobService.destinationName("archive/", "incoming/report.csv", "incoming/");
        assertThat(dest).isEqualTo("archive/report.csv");
    }

    @Test
    void shouldHandleLeadingSlashesInPrefixAndKeys() {
        var dest = BlobService.destinationName("/tasks/azure-move", "/tasks/azure-from/sub/file.yml", "/tasks/azure-from");
        assertThat(dest).isEqualTo("/tasks/azure-move/sub/file.yml");
    }

    @Test
    void shouldFallbackToFilenameWhenPrefixIsNull() {
        var dest = BlobService.destinationName("/tasks/azure-move", "/tasks/azure-from/file.yml", null);
        assertThat(dest).isEqualTo("/tasks/azure-move/file.yml");
    }

    @Test
    void shouldFallbackToFilenameWhenPrefixDoesNotMatch() {
        var dest = BlobService.destinationName("archive/", "other-folder/file.csv", "incoming");
        assertThat(dest).isEqualTo("archive/file.csv");
    }

    @Test
    void shouldHandleEmptyMoveToKey() {
        var dest = BlobService.destinationName("", "incoming/2023/report.csv", "incoming/");
        assertThat(dest).isEqualTo("2023/report.csv");
    }

    @Test
    void shouldHandleEmptySubpath() {
        var dest = BlobService.destinationName("archive/", "", "incoming/");
        assertThat(dest).isEqualTo("archive");
    }
}
