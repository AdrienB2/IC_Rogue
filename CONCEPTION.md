# MP2 - ICRogue - CONCEPTION

## HUD
La classe HUD est une `Entity` qui s'occupe d'afficher tous les éléments du HUD (niveau de vie du joueur, les objets qu'il possède, ...)
Cette classe comme toutes les autres liés au HUD se trouvent dans le package `game.icrogue.hud`
### HeartIconHUD
Cette classe est une `Entity`. Cette classe représente un cœur qui sera affiché dans la barre de vie du joueur. Cette classe contient les 3 sprites possibles :
![coeur vide, demi coeur, coeur complet](src/main/res/images/sprites/zelda/heartDisplay.png)
### PlayerHpDisplayHUD
Cette classe est une `Entity`. Elle contient un array de 5 `HeartIconHUD`. Elle s'occupe de choisir le sprite à afficher pour chaque cœur en fonction du nombre de point de vie du joueur.

Une instance de cette classe est utilisée dans la classe HUD pour être affichée.

___

## Items
Nous avons ajouté plusieurs classes et sous-classe pour les items.

Toutes les classes ci-dessous se trouve dans `game.icrogue.items` 

### Heal
Cette classe abstraite est un `Item` qui contient un attribut représentant le nombre de point de vie donné au joueur lorsqu'il collecte cette item.

Voici les sous-classes conctrètes de `Heal`:

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
- FlameSkull : se déplace en direction le joueur et donne 1pt de damage s'il le touche (fonctionne +ou-)