import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './niveau.reducer';

export const Niveau = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const niveauList = useAppSelector(state => state.niveau.entities);
  const loading = useAppSelector(state => state.niveau.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="niveau-heading" data-cy="NiveauHeading">
        <Translate contentKey="isimmManagerApp.niveau.home.title">Niveaus</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="isimmManagerApp.niveau.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/niveau/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="isimmManagerApp.niveau.home.createLabel">Create new Niveau</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {niveauList && niveauList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="isimmManagerApp.niveau.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('classe')}>
                  <Translate contentKey="isimmManagerApp.niveau.classe">Classe</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('classe')} />
                </th>
                <th className="hand" onClick={sort('tp')}>
                  <Translate contentKey="isimmManagerApp.niveau.tp">Tp</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('tp')} />
                </th>
                <th className="hand" onClick={sort('td')}>
                  <Translate contentKey="isimmManagerApp.niveau.td">Td</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('td')} />
                </th>
                <th>
                  <Translate contentKey="isimmManagerApp.niveau.semestre">Semestre</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {niveauList.map((niveau, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/niveau/${niveau.id}`} color="link" size="sm">
                      {niveau.id}
                    </Button>
                  </td>
                  <td>{niveau.classe}</td>
                  <td>{niveau.tp}</td>
                  <td>{niveau.td}</td>
                  <td>{niveau.semestre ? <Link to={`/semestre/${niveau.semestre.id}`}>{niveau.semestre.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/niveau/${niveau.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/niveau/${niveau.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/niveau/${niveau.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="isimmManagerApp.niveau.home.notFound">No Niveaus found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Niveau;
