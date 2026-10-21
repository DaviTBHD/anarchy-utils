# Anarchy Utils

Mod de código aberto para Minecraft com Fabric, com controles de som para o Wither e o sino em um painel dentro do jogo.

## Recursos

| Controle | Ajuste | Padrão |
| --- | --- | --- |
| Volume do Wither | 0–100%, com botão para silenciar | 0% (silenciado) |
| Volume do sino e da ressonância | 0–300% | 300% |
| Alcance do sino e da ressonância | 16–256 blocos | 128 blocos |

As mudanças são aplicadas aos próximos sons reproduzidos. O painel também permite restaurar os valores padrão.

## Requisitos

Versões usadas pelo projeto:

- Minecraft **26.2**
- Fabric Loader **0.19.5** ou superior
- Fabric API **0.161.0+26.2**
- Java **25**

## Instalação

1. Configure uma instalação do Minecraft 26.2 com Fabric Loader.
2. Coloque o Fabric API e o arquivo `anarchyutils-1.1.jar` na pasta `mods` dessa instalação.
3. Inicie o jogo com o perfil Fabric.

Para gerar o arquivo do mod a partir do código, siga a seção de desenvolvimento abaixo. Use o JAR normal de `build/libs/`, não o arquivo terminado em `-sources.jar`.

## Como usar

Dentro do mundo, pressione **Shift direito** para abrir o painel.

- Use o botão de silenciar ou a barra de volume para ajustar o Wither.
- Ajuste o volume e o alcance do sino nas barras correspondentes.
- Clique em **Concluido** ou pressione **Esc** para fechar e salvar.

A tecla pode ser alterada nas configurações de controles do Minecraft.

## Configurações

As preferências são carregadas ao iniciar o cliente e salvas ao fechar o painel em:

```text
config/anarchyutils-sounds.properties
```

Exemplo com os valores padrão:

```properties
witherVolume=0.0
bellVolume=3.0
bellRangeBlocks=128
```

Os volumes nesse arquivo são multiplicadores: `1.0` corresponde a 100% e `3.0` a 300%. Para editar manualmente, feche o jogo primeiro.

## Alcance no singleplayer e multiplayer

O alcance depende tanto da reprodução no cliente quanto da distância em que o servidor envia o evento do som.

**Singleplayer:** o mod altera os dois lados, incluindo o servidor integrado. O volume diminui conforme a distância até o alcance configurado.

**Servidores de terceiros:** os controles de volume funcionam no cliente. A ampliação de alcance só funciona para eventos que o servidor envia; configurar 128 blocos não garante ouvir a essa distância. Ter o chunk carregado ou aumentar a distância de renderização não elimina esse limite.

**Servidor com o mod:** a alteração de envio amplia o alcance do sino para pelo menos 128 blocos por padrão. Atualmente, o painel e a leitura do arquivo de configuração existem apenas no cliente; um servidor dedicado usa o valor padrão de `BellSoundSettings.rangeBlocks`, sem sincronização automática com o painel dos jogadores.

## Desenvolvimento

Com Java 25 disponível no `JAVA_HOME`, execute na raiz do projeto:

| Ação | Windows (PowerShell) | Linux / macOS |
| --- | --- | --- |
| Compilar o mod | `.\gradlew.bat build` | `./gradlew build` |
| Abrir o cliente de desenvolvimento | `.\gradlew.bat runClient` | `./gradlew runClient` |

O JAR compilado fica em `build/libs/`. O primeiro build pode precisar de acesso à internet para baixar dependências e recursos.

O cliente de desenvolvimento normalmente usa uma conta sem autenticação; para entrar em servidores que exigem uma conta autenticada, use seu launcher com o mod instalado.

## Teste rápido no singleplayer

1. Entre em um mundo com comandos habilitados e abra o painel com Shift direito.
2. Com o Wither silenciado, execute `/playsound minecraft:entity.wither.ambient master @s`. Aumente o volume do Wither e repita para comparar.
3. Acione um sino repetidamente com redstone e afaste-se para comparar diferentes valores de alcance. Mantenha a área do sino carregada e a redstone funcionando durante o teste.
4. Feche o painel e reinicie o jogo para conferir se as preferências foram mantidas.

## Licença

Distribuído sob **CC0-1.0**. Consulte [LICENSE](LICENSE).
