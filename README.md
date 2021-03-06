**HappyPet** is a Bukkit plugin that allows players to more easily manage vanilla pets;
ocelots, wolves and horses. It is a derivative of [HappyDog](https://github.com/JohnGuru/HappyDog)
by [JohnGuru](https://github.com/JohnGuru).

## Features

* [See information of a pet by hitting it with a wand (default: bone)](http://i.imgur.com/THldv6i.png)
* Change the owner of your own or any pet
* Free your own or any pet into the wild
* Calm your own or any wolf

Unlike other pet plugins, this plugin will **not** aim to:

* Allow non-tameable animals such as sheep, cows or mobs to be kept as pets
* Provide non-vanilla pet features such as multi-owner, attacks, pokeballs, etc
* Provide difficult features such as pet summoning
* Replicate vanilla features (e.g. collar color changing or pet renaming)
* Go beyond commands or wands for pet control (e.g. GUI)

## Permissions

*To get started, you only need to grant the `happypet.player` permission for pet-owner only
actions (e.g. can only calm, free or transfer ownership of own pets) and `happypet.admin`
for all admin actions (e.g. affect any pet, including wild, and reload)*

See [`/src/main/resources/plugin.yml`](https://github.com/Gamealition/HappyPet/blob/master/src/main/resources/plugin.yml)
for full permission list.

## Commands

* `/happypet owner [player]` - Give away a pet to somebody else
* `/happypet calm` - Calms down an angry wolf
* `/happypet free` - Frees a pet into the wild
* `/happypet wand` - Find out what the pet wand to set to
* `/happypet reload` - Reloads config.yml

# Building, debugging and debug logging

For instructions and screenshots on how to. . .

* Compile this plugin from scratch
* Build a JAR of this plugin
* Debug this plugin on a server
* Enable debug logging levels such as `FINE` and `FINER`

. . .[please follow the linked guide on this Google document.](https://docs.google.com/document/d/1TTDXG7IZ9M0D2-rzbILAWg1CKjynHK8fNGxbf3W4wBk/view)

## License
As HappyPet is a derivative of HappyDog by JohnGuru, HappyPet is licensed the same under
the GNU General Public License 2.0 license. Please see `LICENSE.md` or
[this website](https://www.gnu.org/licenses/gpl-2.0.html) for the full license.
