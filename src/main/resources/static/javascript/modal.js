
document.addEventListener("DOMContentLoaded", function () {



    const modalElement = document.getElementById("shortUrlResultModal");

    if (modalElement) {
        const shortUrl = modalElement.dataset.shortUrl;

        if (window.bootstrap) {
            bootstrap.Modal
                .getOrCreateInstance(modalElement)
                .show();
        }

        const copyButton = document.getElementById("copyShortUrlButton");
        const copyFeedback = document.getElementById("copyFeedback");

        async function copyText(text) {
            if (navigator.clipboard && window.isSecureContext) {
                await navigator.clipboard.writeText(text);
                return;
            }

            const textarea = document.createElement("textarea");
            textarea.value = text;
            textarea.style.position = "fixed";
            textarea.style.opacity = "0";

            document.body.appendChild(textarea);
            textarea.select();

            const copied = document.execCommand("copy");
            textarea.remove();

            if (!copied) {
                throw new Error("Não foi possível copiar o link.");
            }
        }

        if (copyButton) {
            copyButton.addEventListener("click", async function () {
                try {
                    await copyText(copyButton.dataset.copyUrl || shortUrl);

                    if (copyFeedback) {
                        copyFeedback.textContent =
                            "Link copiado com sucesso!";
                    }

                    copyButton.innerHTML =
                        '<i class="bi bi-check2" aria-hidden="true"></i>';

                    copyButton.setAttribute("aria-label", "Link copiado");
                } catch (error) {
                    if (copyFeedback) {
                        copyFeedback.textContent =
                            "Não foi possível copiar automaticamente. Selecione o link e copie manualmente.";
                    }
                }
            });
        }

        const qrContainer = document.getElementById("resultQrCode");

        if (qrContainer && shortUrl && window.QRCode) {
            new QRCode(qrContainer, {
                text: shortUrl,
                width: 220,
                height: 220,
                colorDark: "#172337",
                colorLight: "#ffffff",
                correctLevel: QRCode.CorrectLevel.H
            });
        }

        const downloadButton =
            document.getElementById("downloadQrCodeButton");

        if (downloadButton && qrContainer) {
            downloadButton.addEventListener("click", function () {
                const canvas = qrContainer.querySelector("canvas");
                const image = qrContainer.querySelector("img");

                const imageUrl = canvas
                    ? canvas.toDataURL("image/png")
                    : image?.src;

                if (!imageUrl) {
                    return;
                }

                const downloadLink = document.createElement("a");
                downloadLink.href = imageUrl;
                downloadLink.download = "minurl-qrcode.png";

                document.body.appendChild(downloadLink);
                downloadLink.click();
                downloadLink.remove();
            });
        }
    }



    const toggleButton =
        document.getElementById("toggleExpirationButton");

    const expirationPanel =
        document.getElementById("expirationPanel");

    if (toggleButton && expirationPanel) {
        toggleButton.addEventListener("click", function () {
            const isExpanded =
                toggleButton.getAttribute("aria-expanded") === "true";

            toggleButton.setAttribute(
                "aria-expanded",
                String(!isExpanded)
            );

            expirationPanel.hidden = isExpanded;

            if (!isExpanded) {
                expirationPanel.querySelector("input")?.focus();
            }
        });
    }

});
