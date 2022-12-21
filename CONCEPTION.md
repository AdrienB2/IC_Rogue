# MP2 - ICRogue - CONCEPTION

## HUD
La classe HUD est une `Entity` qui s'occupe d'afficher tous les éléments du HUD (niveau de vie du joueur, les objets qu'il possède, ...)
Cette classe comme toutes les autres liés au HUD se trouvent dans le package `game.icrogue.hud`

### PlayerHpDisplayHUD
Cette classe est une `Entity`. Elle contient un array de 5 `HeartIconHUD`. Elle s'occupe de choisir le sprite à afficher pour chaque cœur en fonction du nombre de point de vie du joueur.
#### HeartIconHUD
Cette classe est une `Entity`. Cette classe représente un cœur qui sera affiché dans la barre de vie du joueur. Cette classe contient les 3 sprites possibles :
![coeur vide, demi coeur, coeur complet](src/main/res/images/sprites/zelda/heartDisplay.png)

Une instance de cette classe est utilisée dans la classe HUD pour être affichée.

___

## Items
Nous avons ajouté plusieurs classes et sous-classe pour les items.

Toutes les classes ci-dessous se trouve dans `game.icrogue.items` 

### Heal
Cette classe abstraite est un `Item` qui contient un attribut représentant le nombre de points de vie donné au joueur lorsqu'il collecte cette item.

Voici les sous-classes concretes de `Heal`:

#### Heart   
Cette classe est une sous-classe concrète de `Heal` qui donne un point de vie au joueur

---

## ICRogueAreaGraph
Cette classe hérite de `AreaGraph` et permet de créer automatiquement un node sur chaque cell en fonction de la largeur et de la hauteur de la grille.

Cette classe se trouve dans `game.icrogue`

---

## Ennemis
Nous avons ajouté different ennemis. Toutes les classes se trouvent dans `game.icrogue.actor.enemies`:
- Log : se déplace en suivant un trajet prédéfinit et tire des flèches
- Boss : se téléporte de façon aléatoire et pose des bombes

---

## Génération aléatoire de la salle `Level0TurretRoom`
Pour ajouter une part d'aléatoire à la génération de la salle, nous avons ajouté décidé de générer "aléatoirement" les positions des tourelles.
Nous avons défini une liste de disposition possible pour les tourelles et nous choisissons une disposition aléatoirement. La liste `Orientation[][][] TURRET_DIRECTIONS` contient pour chaque disposition possible, les directions dans lesquelles chaque tourelle doit tirer.

---
### Bombe
La classe bombe représente une bombe qui explose au bout de 3 secondes. Elle se trouve dans `game.icrogue.projectiles`. Nous l'avons placée dans le package projectiles, même s'il ne s'agit pas formellement d'un projectile (elle n'hérite pas de la classe `Projectile`), car il nous semblait plus logique de la placer ici.

## Screens
Nous avons ajouté 3 écrans :
- `WinScreen` : affiché lorsque le joueur a gagné
- `GameOverScreen` : affiché lorsque le joueur a perdu
- `PauseScreen` : affiché lorsque le joueur appuie sur la touche ESCAPE
Ces 3 écrans se trouvent dans `game.icrogue.screens` l'affichage de ces écrans se fait dans `ICRogue`.
Le texte est affiché en utilisant un `Text` par lettre. Cela est dû à un problème d'affichage de texte que nous avons rencontré ([c.f. la question sur EdStem](https://edstem.org/eu/courses/53/discussion/15610?comment=27297))