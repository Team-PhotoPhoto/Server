import defaultTile from './res/assets/tile_default.png';
import redTile from './res/assets/tile_red.png';
import bluelineTile from './res/assets/tile_blueline.png';
import flowerTile from './res/assets/tile_flower.png';
import cementTile from './res/assets/tile_cement.png';

import defaultBottom from './res/assets/bottom_default.png';
import redBottom from './res/assets/bottom_red.png';
import bluelineBottom from './res/assets/bottom_blueline.png';
import flowerBottom from './res/assets/bottom_flower.png';
import cementBottom from './res/assets/bottom_cement.png';

import defaultBar from './res/assets/bar_default.png';
import redBar from './res/assets/bar_red.png';
import bluelineBar from './res/assets/bar_blueline.png';
import flowerBar from './res/assets/bar_flower.png';
import cementBar from './res/assets/bar_cement.png';

import defaultFrameMock from './res/assets/frame_default_mock.png';
import yellowFrameMock from './res/assets/frame_yellow_mock.png';
import blueFrameMock from './res/assets/frame_blue_mock.png';
import pinkFrameMock from './res/assets/frame_pink_mock.png';
import blackFrameMock from './res/assets/frame_black_mock.png';

import defaultFrame from './res/assets/frame_default.png';
import yellowFrame from './res/assets/frame_yellow.png';
import blueFrame from './res/assets/frame_blue.png';
import pinkFrame from './res/assets/frame_pink.png';
import blackFrame from './res/assets/frame_black.png';

export function getLayoutSet(layoutType) {
    switch (layoutType) {
        case 'SQUARE':
        return {
            tile: redTile,
            bottom: redBottom,
            bar: redBar,
        };
        case 'STRIPE':
        return {
            tile: bluelineTile,
            bottom: bluelineBottom,
            bar: bluelineBar,
        };
        case 'FLOWER':
        return {
            tile: flowerTile,
            bottom: flowerBottom,
            bar: flowerBar,
        };
        case 'LINE':
        return {
            tile: cementTile,
            bottom: cementBottom,
            bar: cementBar,
        };
        default:
        return {
            tile: defaultTile,
            bottom: defaultBottom,
            bar: defaultBar,
        };
    }
}

export function getAllTiles() {
    return {
        ZIGZAG: defaultTile,
        SQUARE: redTile,
        STRIPE: bluelineTile,
        FLOWER: flowerTile,
        LINE: cementTile,
    };
}

export function getAllFrameMocks() {
    return {
        BROWN: defaultFrameMock,
        GOLD: yellowFrameMock,
        BLUE: blueFrameMock,
        LAVENDER: pinkFrameMock,
        BLACK: blackFrameMock,
    };
}

export function getFrameByType(frameType) {
    switch (frameType) {
        case 'GOLD':
        return yellowFrame;
        case 'BLUE':
        return blueFrame;
        case 'LAVENDER':
        return pinkFrame;
        case 'BLACK':
        return blackFrame;
        default:
        return defaultFrame;
    }
}
