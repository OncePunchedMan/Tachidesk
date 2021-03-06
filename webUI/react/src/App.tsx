import React from 'react';
import {
    BrowserRouter as Router, Route, Switch,
} from 'react-router-dom';
import NavBar from './components/NavBar';
import Home from './screens/Home';
import Sources from './screens/Sources';
import Extensions from './screens/Extensions';
import MangaList from './screens/MangaList';
import Manga from './screens/Manga';
import Reader from './screens/Reader';
import Search from './screens/Search';

export default function App() {
    return (
        <Router>
            <NavBar />

            <Switch>
                <Route path="/search">
                    <Search />
                </Route>
                <Route path="/extensions">
                    <Extensions />
                </Route>
                <Route path="/sources/:sourceId/popular/">
                    <MangaList popular />
                </Route>
                <Route path="/sources/:sourceId/latest/">
                    <MangaList popular={false} />
                </Route>
                <Route path="/sources">
                    <Sources />
                </Route>
                <Route path="/manga/:mangaId/chapter/:chapterId">
                    <Reader />
                </Route>
                <Route path="/manga/:id">
                    <Manga />
                </Route>
                <Route path="/">
                    <Home />
                </Route>
            </Switch>
        </Router>
    );
}
