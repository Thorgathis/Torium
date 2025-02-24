# Torium

<a href="https://modrinth.com/plugin/torium">
    <img src=https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/available/modrinth_64h.png
    />
</a>
<a href="https://github.com/Thorgathis/Torium">
    <img src=https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/available/github_64h.png />
</a>
<a href="https://papermc.io/software/velocity">
    <img src=https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/supported/velocity_64h.png
    />
</a>

**Torium** is a versatile plugin for Velocity-based proxy servers, designed to provide administrators with powerful tools for managing players and servers. The plugin easily integrates into your system, is configurable via files, and supports multiple languages, making it an ideal choice for servers of any size.

## 📝 Commands
Torium offers a set of commands to manage players and servers, simplifying administrative tasks:
- **/alert** — Sends a global message to all players on the proxy.
- **/find** — Locates a player and shows which server they are on.
- **/send** — Moves a player to a specified server.
- **/lobby** — Teleports a player to the lobby server.
- **/list** — Displays a list of all servers and players.
- **/treload** — Reloads the plugin's configuration and language files.

## 🔒 Permissions
Torium uses a permission system to control who can execute specific commands. Some examples include:
- **torium.command.alert** — Access to `/alert`.
- **torium.command.find** — Access to `/find`.
- **torium.command.send** — Access to `/send`.
- **torium.command.lobby** — Access to `/lobby`.
- **torium.command.list** — Access to `/list`.
- **torium.command.reload** — Access to `/treload`.

## ⚙️ Configuration
Torium provides flexible configuration via the `config.toml` file, allowing you to tailor the plugin to your server's needs:
- Enable or disable commands.
- Customize message formats and command outputs.
- Set the lobby server for the `/lobby` command.
- Adjust proxy ping responses to show the total number of players across all servers.
- etc.

All adjustments can be made quickly and easily through configuration files.

## 🌍 Multi-Language Support
Torium supports multiple languages, including English and Russian. You can add your own custom language files by placing them in the `lang` folder. This makes it easy to adapt the plugin for your audience and use it in international communities.

## 🔧 Utils
Torium offers useful utilities that can significantly enhance your server’s functionality:
- **Proxy Ping Modification** — Adjusts the proxy ping response to display the total number of players across all servers, not just the current one.
- **Server List Customization** — Control the visibility of empty servers in the `/list` command through configuration, helping to avoid interface clutter.
- etc.

These utilities allow you to better manage your server and provide players with accurate information.

## ⚡ Installation
Installing Torium is quick and easy:
1. Download the plugin from Modrinth.
2. Place the `Torium-X.X.jar` file in your proxy's `plugins` folder.
3. Restart the server.
4. Configure the plugin via the provided configuration files.

After that, Torium is ready to use!

---

**Torium** is the perfect choice for administrators who want to simplify proxy management and improve player interactions. Easy to set up, highly flexible, and supporting multiple languages, Torium is an excellent tool for servers of any size.